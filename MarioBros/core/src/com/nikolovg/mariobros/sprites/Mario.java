package com.nikolovg.mariobros.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.sprites.enemies.Enemy;
import com.nikolovg.mariobros.sprites.enemies.Turtle;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class Mario extends Sprite{
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation bigMarioRun;
    private Animation growMario;

    private boolean runningRight;
    private float stateTimer;
    private boolean isMarioBig;
    private boolean runGrowAnimation;
    private boolean isItTimeToDefineBigMario;   //boolean name OP
    private boolean isItTimeToReDefineMario;
    private boolean isMarioDead;

    public Mario(PlayScreen screen){

        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //get the 1st 2nd 3rd image from little_mario
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        //get images for bigMario
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        //growing mario animation cycle between half bigMario and bigMario frames
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        // jumping texture regions
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);


        //texture regions for standing mario
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        //texture region for dead mario
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0 ,16, 16);

        defineMario();

        setBounds(0, 0, 16/ MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        //update sprite position to follow the Box2D body and check if mario is big or not
        if(isMarioBig){
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 -6 /MarioBros.PPM);
        }
        else {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        //update the sprite with the correct frame for current state
        setRegion(getFrame(dt));
        /*
        We need these boolean "triggers" to tell us
        that an event such as mario picking up a mushroom or mario being hit by an enemy
        happened since it is not possible to execute
        these methods when the 2d physics is being calculated.
        We are destroying and recreating Box2D bodies to change mario from big to small
        and vice versa hence we need to save the information that the event occurred
        in order to be able to execute these methods in between the world steps
        upon the update.
         */
        if(isItTimeToDefineBigMario){
            defineBigMario();
        }
        if(isItTimeToReDefineMario){
            reDefineMario();
        }

         if(b2body.getPosition().y < 0 && !isMarioDead){
           isMarioDead = true;
           killMarioByFall();
         }


    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                // don't give the method "true" as a parameter because the animation is not loopable
                region = growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                //check to see if mario is big and return the correct state, same below for other states
                region = isMarioBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = isMarioBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = isMarioBig ? bigMarioStand : marioStand;
                break;
        }
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){

        if(isMarioDead){
            return State.DEAD;
        }
        else if(runGrowAnimation){
            return State.GROWING;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else{
            return State.STANDING;
        }
    }

    public void reDefineMario(){
        //create a vector with the position of mario in order to create a new body in the same position
        Vector2 position = b2body.getPosition();
        //destroy mario
        world.destroyBody(b2body);


        //create a body definition for mario
        BodyDef bDef = new BodyDef();
        bDef.position.set(position);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        // define Mario fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Define marios head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //define marios feet
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-3 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(3 / MarioBros.PPM, -6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        //setting a boolean to insure the recreation only happens once
        isItTimeToReDefineMario = false;

    }

    public void defineBigMario(){
        //save the mario b2Body current position and destroy the little mario body in order to create a new bigger one
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        //create a body definition for mario
        BodyDef bDef = new BodyDef();
        //set the new body position to the old coordinates but add some in the y axis as mario is growing up
        bDef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        // define Mario fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);

        //Define marios head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //define marios feet
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4 / MarioBros.PPM, -20 / MarioBros.PPM), new Vector2(4 / MarioBros.PPM, -20 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        // the boolean insures the growing definition will be executed only once;
        isItTimeToDefineBigMario = false;
    }


    public void defineMario(){
        //create a body definition for mario
        BodyDef bDef = new BodyDef();
        bDef.position.set(32/ MarioBros.PPM,32/ MarioBros.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        // define Mario fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Define marios head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //define marios feet
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-3 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(3 / MarioBros.PPM, -6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void grow(){
        runGrowAnimation = true;
        isMarioBig = true;
        isItTimeToDefineBigMario = true;
        //set sprite size to be 16 by 32 for big Mario
        setBounds(getX(), getY(), getWidth(),getHeight()*2);
        MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public boolean getIsMarioBig(){
        return isMarioBig;
    }


    public void hit(Enemy enemy){
        if(enemy instanceof Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        }
        else {
            if (isMarioBig) {
                isMarioBig = false;
                isItTimeToReDefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
                MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                isMarioDead = true;
                //create a filter and attach it to mario so that we don't have any collision anymore
                //which allows mario to fall through everything upon death as per the original game
                Filter filter = new Filter();
                filter.maskBits = MarioBros.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }
    }

    public boolean getisMarioDead(){
        return isMarioDead;
    }

    public void killMarioByFall(){

            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            isMarioDead = true;
            //create a filter and attach it to mario so that we don't have any collision anymore
            //which allows mario to fall through everything upon death as per the original game
            Filter filter = new Filter();
            filter.maskBits = MarioBros.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            b2body.applyLinearImpulse(new Vector2(0, 8f), b2body.getWorldCenter(), true);

    }


    public float getStateTimer(){
        return stateTimer;
    }
}
