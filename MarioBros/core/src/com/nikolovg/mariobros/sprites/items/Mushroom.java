package com.nikolovg.mariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.screens.PlayScreen;

/**
 * Created by Freeware Sys on 8/19/2016.
 */
public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0 , 0, 16, 16);
        velocity = new Vector2(0,0);
    }

    @Override
    public void defineItem() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void useItem() {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() /2);
        body.setLinearVelocity(velocity);
    }
}
