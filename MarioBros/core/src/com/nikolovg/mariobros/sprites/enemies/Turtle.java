package com.nikolovg.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.sprites.Mario;

/**
 * Created by Freeware Sys on 27.8.2016 г..
 */
public class Turtle  extends Enemy{
    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;
    public enum State{WALKING, STANDING_SHELL, MOVING_SHELL}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private static final float DEFAULT_TURTLE_HEAD_RESTITUTION = 1f;
    private static final float MATCH_TURTLE_SPRITE_WITH_B2D_BODY = 8;
    private static final float DEFAULT_TURTLE_SHELL_TIME = 5;
    private static final float DEFAULT_TURTLE_BODY_RADIUS = 6;
    private static final float DEFAULT_TURTLE_WIDTH = 16;
    private static final float DEFAULT_TURTLE_HEIGHT = 24;


    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        // walking animation frames
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        //shell sprite
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        // we tell the render engine where should the sprite be and how big it should be
        setBounds(getX(),getY(),DEFAULT_TURTLE_WIDTH / MarioBros.PPM, DEFAULT_TURTLE_HEIGHT / MarioBros.PPM);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;


    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        //setting the position of the bodyDefinition with x and y coordinates taken from the tiled Map
        bDef.position.set(getX(), getY());
        // DinamicBody means it can be affected by forces in the world such as gravity
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(DEFAULT_TURTLE_BODY_RADIUS / MarioBros.PPM);
        // setting a categoryBit means that we are telling the collision listener this shape is an enemy
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        // setting the maskBits means we are telling the collision listener that this fixture will collide with those below
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        // creating vectors to draw a trapezoid shape to act as the monters head
        //bottom left
        vertice[0] = new Vector2(-5, 8).scl(1/MarioBros.PPM);
        //bottom right
        vertice[1] = new Vector2(5, 8).scl(1/MarioBros.PPM);
        //top left
        vertice[2] = new Vector2(-3, 3).scl(1/MarioBros.PPM);
        //top right
        vertice[3] = new Vector2(3, 3).scl(1/MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = DEFAULT_TURTLE_HEAD_RESTITUTION;
        //same as above - setting the monster head and telling the collision listener that marios feet will collide with the head
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = MarioBros.MARIO_FEET_BIT;
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnHead(Mario mario) {
        // transition from turtle state to shell state and remove its velocity upon hit from mario on the head
        if(currentState != State.STANDING_SHELL){
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
        else{
            // if mario kicked the turtle right or left give it impulse in said direction when the turtle is in SHELL state
            kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
        }


    }

    public TextureRegion getFrame(float dt){
        //we need to update the turtle but since it can have multiple states
        //we need to know which state are we in and how long has the turtle been in the current state
        // the method returns the correct texture region from the sprite atlas so we can correctly draw
        // turtle animations for any given state in any given frame
        TextureRegion region;

        switch (currentState){
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;

            case  WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        //we check on each frame if the turtle is moving left or right and we
        //flip the sprite to face in the correct direction according to movement
        if(velocity.x > 0 && region.isFlipX() == false){
            region.flip(true , false);
        }
        if(velocity.x < 0 && region.isFlipX() == true){
            region.flip(true , false);
        }
        // if the current state is the same as the previous state add the deltaTime(time it takes for each frame to be rendered on screen)
        // to the stateTime so we know how long has the turtle been in its current state
        // if the current state does not equal the state in the previous frame that means we switched states and we make the timer zero again
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        // if the turtle has been in shell form and not moving for a given time
        // return it to turtle state and give it velocity again
        if(currentState == State.STANDING_SHELL && stateTime > DEFAULT_TURTLE_SHELL_TIME){
            currentState = State.WALKING;
            velocity.x = 1;
        }
        // set the position of our sprite to match our B2D body between each frame and we need this constant since
        // the turtle sprite is a bit odd and wont match very well with the B2D body
        setPosition(b2Body.getPosition().x - getWidth()/2, b2Body.getPosition().y - MATCH_TURTLE_SPRITE_WITH_B2D_BODY/ MarioBros.PPM);
        b2Body.setLinearVelocity(velocity);
    }

    public void kick(int speed){
        // called when mario touches a turtle which is currently in shell form
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState(){
        return currentState;
    }
}
