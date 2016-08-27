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

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        // create ground body/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioBros.PPM, (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM);
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth()/2/ MarioBros.PPM , rect.getHeight()/2/ MarioBros.PPM);
            fDef.shape = shape;
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
        //create brick body/fixture
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object);

        }
        //create coin body/fixture
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);

        }

        //create goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX()/MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }

        // create turtles
        turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX()/MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }
    }

    public Array<Goomba> getGoombas(){
        return goombas;
    }
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }

}
