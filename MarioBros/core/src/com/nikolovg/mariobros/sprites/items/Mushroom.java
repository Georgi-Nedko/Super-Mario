package com.nikolovg.mariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.sprites.Mario;

/**
 * Created by Freeware Sys on 8/19/2016.
 */
public class Mushroom extends Item {
    private static final float DEFAULT_MUSHROOM_BODY_RADIUS = 6;

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        // take the mushroom sprite from the atlas
        setRegion(screen.getAtlas().findRegion("mushroom"), 0 , 0, 16, 16);
        // give the mushroom velocity
        velocity = new Vector2(0.7f,0);
    }

    @Override
    public void defineItem() {
        BodyDef bDef = new BodyDef();
        //set the position of the b2d body
        bDef.position.set(getX(), getY());
        // dynamic body means that it is affected by forces in the world such as gravity or impulses
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fdef = new FixtureDef();
        // create the shape for the mushroom fixture
        CircleShape shape = new CircleShape();
        shape.setRadius(DEFAULT_MUSHROOM_BODY_RADIUS / MarioBros.PPM);
        // tell the collision listener that this is an item
        fdef.filter.categoryBits = MarioBros.ITEM_BIT;
        // tell the collision listener that this item can collide with the objects bellow
        fdef.filter.maskBits = MarioBros.MARIO_BIT | MarioBros.OBJECT_BIT | MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void useItem(Mario mario) {
        // when mario picks up the mushroom we want him to grow and we want the mushroom to be destroyed
        destroy();
        mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        // we update the position of the mushroom on each frame
        setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() /2);
        // we need to keep giving it velocity or else it will stop moving
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
