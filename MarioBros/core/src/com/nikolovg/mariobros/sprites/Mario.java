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
import com.nikolovg.mariobros.scenes.Hud;
import com.nikolovg.mariobros.screens.MainMenuScreen;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.screens.SettingsScreen;
import com.nikolovg.mariobros.sprites.enemies.Enemy;
import com.nikolovg.mariobros.sprites.enemies.Turtle;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class Mario extends Sprite{


    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private MarioBros game;
    private PlayScreen screen;
    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation bigMarioRun;
    private Animation growMario;

    public static boolean isFinished;
    private boolean runningRight;
    private float stateTimer;
    private boolean isMarioBig;
    private boolean runGrowAnimation;
    private boolean isItTimeToDefineBigMario;   //boolean name OP
    private boolean isItTimeToReDefineMario;
    private boolean isMarioDead;
    private static final float DEFAULT_MARIO_WIDTH = 16;
    private static final float DEFAULT_MARIO_HEIGHT = 16;
    private static final float BIG_MARIO_SPRITE_POSITION_MODIFIER = 8;
    private static final float DEFAULT_LITTLE_MARIO_BODY_RADIUS = 5;
    private static final float DEFAULT_BIG_MARIO_BODY_RADIUS = 6;
    private static final float DEFAULT_BIG_MARIO_SPRITE_HEIGHT = -22;
    private static final float DEFAULT_MARIO_START_COORDINATE_X = 256;
    private static final float DEFAULT_MARIO_START_COORDINATE_Y = 32;

    public Mario(PlayScreen screen, MarioBros game){
        this.game = game;
        this.world = screen.getWorld();
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        isFinished = false;

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

        setBounds(0, 0, DEFAULT_MARIO_WIDTH/ MarioBros.PPM, DEFAULT_MARIO_HEIGHT / MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        //update sprite position to follow the Box2D body and check if mario is big or not
        if(isMarioBig){
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 -BIG_MARIO_SPRITE_POSITION_MODIFIER /MarioBros.PPM);
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
        if(screen.getHud().getWorldTimer() == 0){
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
        // checking if mario is running left or right and flipping his sprite accordingly so
        // he is always facing in the direction he is moving
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        // if the previous state (in the last frame) is the same as the current state increment the stateTimer
        // if not then we have changed state in between frames and we reset the stateTimer
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
        shape.setRadius(DEFAULT_LITTLE_MARIO_BODY_RADIUS / MarioBros.PPM);
        // telling the collision listener this fixture is mario
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        // telling the collision listener that mario collides with all the below fixtures
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ITEM_BIT | MarioBros.FINISH_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Define marios head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        // telling the collision listener this fixture is marios head
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        // isSensor = true means that the collision listener will detect that a collision should happen
        // but the physics engine will not calculate this collision and the two fixtures will overlap
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //define marios feet
        /*We want this fixture since it is the only one (that I am aware of) which is capable
        * of smooth gliding on top of tiles which have individual hitboxes and if it is not used then
        * the running animation is glitchy because the physics engine detects that for a few frames mario is
        * not technically stepping on any tiles and he is in mid air between the two tiles which triggers the
        * jumping sprite to be put on the mario fixture and this messes up the running animation
        * Having the edge shape there solves the problem since mario can no longer be in mid air and he is
        * always stepping on at least one tile when running
        * */
        EdgeShape feet = new EdgeShape();
        //setting the edge shape to be just short of the radius of mario circle shape body
        feet.set(new Vector2(-4 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(4 / MarioBros.PPM, -6 / MarioBros.PPM));
        // telling the collision listener that this is marios feet
        fdef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
        // telling the collision listener that marios feet will collide with all the below
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
        fdef.shape = feet;
        // we want the physics engine to calculate collision between marios feet and other objects
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
        shape.setRadius(DEFAULT_BIG_MARIO_BODY_RADIUS / MarioBros.PPM);
        // telling the collision listener this fixture is mario
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        // telling the collision listener that mario  will collide with all the below
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ITEM_BIT | MarioBros.FINISH_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        // set the same shape to be below the first one and create it again
        shape.setPosition(new Vector2(0, -(DEFAULT_BIG_MARIO_BODY_RADIUS + 2) / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);

        //Define marios head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        // telling the collision listener this fixture is marios head
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        // isSensor = true means that the collision listener will detect that a collision should happen
        // but the physics engine will not calculate this collision and the two fixtures will overlap
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //define marios feet
        EdgeShape feet = new EdgeShape();
        // setting the edge shape to be below mario
        feet.set(new Vector2(-4 / MarioBros.PPM, DEFAULT_BIG_MARIO_SPRITE_HEIGHT / MarioBros.PPM), new Vector2(4 / MarioBros.PPM, DEFAULT_BIG_MARIO_SPRITE_HEIGHT / MarioBros.PPM));
        // telling the collision listener this fixture is marios feet
        fdef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
        // telling the collision listener this fixture is marios feet
        // telling the collision listener that mario  will collide with all the below
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        // the boolean insures the growing definition will be executed only once;
        isItTimeToDefineBigMario = false;
    }


    public void defineMario(){
        /*
        * for complete documentation
        * check reDefineMario or defineBigMario methods above
        * */

        //create a body definition for mario
        BodyDef bDef = new BodyDef();
        bDef.position.set(DEFAULT_MARIO_START_COORDINATE_X/ MarioBros.PPM,DEFAULT_MARIO_START_COORDINATE_Y/ MarioBros.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        // define Mario fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ITEM_BIT | MarioBros.FINISH_BIT;

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
        feet.set(new Vector2(-4 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(4 / MarioBros.PPM, -6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT
                | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT
                | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void grow(){
        runGrowAnimation = true;
        isMarioBig = true;
        isItTimeToDefineBigMario = true;
        //set sprite size to be 16 by 32 for big Mario so we double the height of little mario
        setBounds(getX(), getY(), getWidth(),getHeight()*2);
        game.manager.get("audio/sounds/powerup.wav", Sound.class).play(SettingsScreen.volumeValue);
    }

    public boolean getIsMarioBig(){
        return isMarioBig;
    }


    public void hit(Enemy enemy){
        //check if the enemy is a turtle and then check if the turtle is in shell form
        if(enemy instanceof Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        }
        else {
            //enemy is a goomba
            if (isMarioBig) {
                //if mario is big press the triggers to make him small in between this frame and the next
                isMarioBig = false;
                isItTimeToReDefineMario = true;
                // reduce the height of the b2d body to 16 again from 32 for little mario
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                game.manager.get("audio/sounds/powerdown.wav", Sound.class).play(SettingsScreen.volumeValue);
            } else {
                //mario is small
                //stop the music
                game.manager.get("audio/music/mario_music.ogg", Music.class).stop();
                //play the dying sound
                game.manager.get("audio/sounds/mariodie.wav", Sound.class).play(SettingsScreen.volumeValue);
                //trigger killing mario on the next frame
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

    public void finishLevel() {
        //we want mario to collide with the finish pole to slide down to the ground and then we remove collision with
        // all but the ground so mario can move to the castle
        Filter filter = new Filter();
        filter.maskBits = MarioBros.GROUND_BIT;
        for(Fixture fixture : b2body.getFixtureList()){
            fixture.setFilterData(filter);
        }
        isFinished = true;
    }

    public boolean getisMarioDead(){
        return isMarioDead;
    }

    public void killMarioByFall(){
            // stop the music and play the dying sound
            game.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            game.manager.get("audio/sounds/mariodie.wav", Sound.class).play(SettingsScreen.volumeValue);
            isMarioDead = true;
            //create a filter and attach it to mario so that we don't have any collision anymore
            //which allows mario to fall through everything upon death as per the original game
            Filter filter = new Filter();
            filter.maskBits = MarioBros.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }


    }


    public float getStateTimer(){
        return stateTimer;
    }
}
