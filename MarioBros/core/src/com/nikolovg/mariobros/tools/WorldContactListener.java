package com.nikolovg.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.sprites.Mario;
import com.nikolovg.mariobros.sprites.enemies.Enemy;
import com.nikolovg.mariobros.sprites.items.Item;
import com.nikolovg.mariobros.sprites.tiles.InteractiveTileObject;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class WorldContactListener implements ContactListener {
    public static boolean isJumpAllowed = true;
    /*
    * This class listens for collisions in the world.
    * Upon a collision it returns two fixtures that have collided.
    * We don't know which fixture is what so we need to switch all
    * possible pairs and check which fixture is which to avoid class cast exceptions.*/
    @Override
    public void beginContact(Contact contact) {
        // this method is called right after the collision has occurred
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        // handling different collisions
        switch (cDef){
            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT){
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                }
                isJumpAllowed = false;
                break;

            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_FEET_BIT:
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                }
                else {
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                }
                break;

            case MarioBros.MARIO_BIT | MarioBros.FINISH_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT){
                    ((Mario) fixA.getUserData()).finishLevel();
                }
                else{
                    ((Mario) fixB.getUserData()).finishLevel();
                }
                break;

            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT){
                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
                }
                else{
                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                }
                break;

            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
                    ((Item)fixA.getUserData()).useItem((Mario) fixB.getUserData());
                }
                else {
                    ((Item) fixB.getUserData()).useItem((Mario) fixA.getUserData());
                }
                break;
        }
    }

    @Override
    // this method is called right after the two bodies have separated and are no longer touching
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                isJumpAllowed = true;
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // here we can calculate something before the collision has ended and while the bodies are still in contact
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // here we should do any calculations that involve what will happen when the two fixtures separate and are no longer touching
    }
}
