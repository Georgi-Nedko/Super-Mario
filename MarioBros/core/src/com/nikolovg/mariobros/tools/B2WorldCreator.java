package com.nikolovg.mariobros.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.sprites.enemies.Enemy;
import com.nikolovg.mariobros.sprites.enemies.Turtle;
import com.nikolovg.mariobros.sprites.tiles.Brick;
import com.nikolovg.mariobros.sprites.tiles.Coin;
import com.nikolovg.mariobros.sprites.enemies.Goomba;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class B2WorldCreator {
    private ArrayList<Goomba> goombas;
    private ArrayList<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        // create ground body/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            // we get the object layer corresponding to the ground and get the individual rectangle objects
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //static body means it can not be affected by forces in the world such as gravity or impulses
            bDef.type = BodyDef.BodyType.StaticBody;
            //position the body where it should be according to the map we have created
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioBros.PPM, (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM);
            body = world.createBody(bDef);
            //we tell the physics engine this shape (a Polygon) is infact a box since that makes it easier to handle it
            // and the physics engine has an easier time calculating for it
            shape.setAsBox(rect.getWidth()/2/ MarioBros.PPM , rect.getHeight()/2/ MarioBros.PPM);
            fDef.shape = shape;
            // we tell the collision listener that these rectangles will be the ground
            fDef.filter.categoryBits = MarioBros.GROUND_BIT;
            body.createFixture(fDef);

        }
        //create pipe body/fixture
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioBros.PPM, (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM);
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth()/2/ MarioBros.PPM , rect.getHeight()/2/ MarioBros.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fDef);
        }

        //create finish body/fixture
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioBros.PPM, (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM);
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth()/2/ MarioBros.PPM , rect.getHeight()/2/ MarioBros.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = MarioBros.FINISH_BIT;
            fDef.filter.maskBits =  MarioBros.MARIO_BIT;
            body.createFixture(fDef);
        }

        //create brick body/fixture
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen, object);
        }
        //create coin body/fixture
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Coin(screen, object);
        }

        //create goombas
        goombas = new ArrayList<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX()/MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }

        // create turtles
        turtles = new ArrayList<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX()/MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }
    }



    public List<Enemy> getEnemies(){
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return Collections.unmodifiableList(enemies);
    }

}
