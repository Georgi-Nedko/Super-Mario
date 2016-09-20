package com.nikolovg.mariobros.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.scenes.Hud;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.screens.SettingsScreen;
import com.nikolovg.mariobros.sprites.Mario;

/**
 * Created by Freeware Sys on 8/19/2016.
 */
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private static final float DEFAULT_GOOMBA_HEAD_RESTITUTION = 0.5f;
    private static final float DEFAULT_GOOMBA_SPRITE_WIDTH = 16;
    private static final float DEFAULT_GOOMBA_SPRITE_HIGHT = 16;
    private static final float DEFAULT_GOOMBA_BODY_RADIUS = 6;
    private static final float DEFAULT_GOOMBA_SPRITE_DRAW_TIME_AFTER_KILL = 1;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        // we tell the render engine where the sprite should be and how big it should be
        setBounds(getX(),getY(), DEFAULT_GOOMBA_SPRITE_WIDTH/ MarioBros.PPM, DEFAULT_GOOMBA_SPRITE_HIGHT / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;
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
        // making a fixture for the goomba body
        CircleShape shape = new CircleShape();
        shape.setRadius(DEFAULT_GOOMBA_BODY_RADIUS / MarioBros.PPM);
        // setting a categoryBit means that we are telling the collision listener this shape is an enemy
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        // setting the maskBits means we are telling the collision listener that this fixture will collide with those below
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        //creating a trapezoid polygon to act as the goombas head and detect when marios feet have collided with the head of the goomba
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-3.5f, 7).scl(1/MarioBros.PPM);
        vertice[1] = new Vector2(3.5f, 7).scl(1/MarioBros.PPM);
        vertice[2] = new Vector2(6f, 5).scl(1/MarioBros.PPM);
        vertice[3] = new Vector2(-6f, 5).scl(1/MarioBros.PPM);

        head.set(vertice);

        fdef.shape = head;
        //restitution is basically "bounciness"
        //the higher the value the more things will bounce off when colliding with this fixture
        fdef.restitution = DEFAULT_GOOMBA_HEAD_RESTITUTION;
        // telling the collision listener that this polygon is the enemy head
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        // telling the collision listener that this fixture will collide with mario and his feet
        fdef.filter.maskBits = MarioBros.MARIO_FEET_BIT | MarioBros.MARIO_BIT;
        // setting all of our above shapes to match this fixture when it is created
        b2Body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        stateTime += dt;
        //when mario hits a goomba we want to remove the B2D body
        // change the sprite to squashed goomba for a certain ammount of time and then remove it
        if(setToDestroy && !destroyed){
            world.destroyBody(b2Body);
            destroyed = true;
            // squashed goomba texture
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0,16,16));
            stateTime = 0;
        }
        else if(!destroyed) {
            // update the position, velocity and animation of the goomba on each frame
            b2Body.setLinearVelocity(velocity);
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
     public void draw(Batch batch){
         if(!destroyed || stateTime < DEFAULT_GOOMBA_SPRITE_DRAW_TIME_AFTER_KILL){
             super.draw(batch);
         }
     }
    @Override
    public void hitOnHead(Mario mario){
        //when mario hits a goomba on the head we use the boolean trigger setToDestroy
        // this is done since we need to excecute the B2D body destruction and sprite change in between frames
        // if we try to do this during rendering it will result in a crash
        setToDestroy = true;
        // we play the correct sound we kill a goomba and give the player 200 points on screen
        screen.getGame().manager.get("audio/sounds/stomp.wav", Sound.class).play(SettingsScreen.volumeValue);
        Hud.addScore(200);
    }

}
