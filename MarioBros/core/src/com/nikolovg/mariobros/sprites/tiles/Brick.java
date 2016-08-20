package com.nikolovg.mariobros.sprites.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.scenes.Hud;
import com.nikolovg.mariobros.screens.PlayScreen;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class Brick extends com.nikolovg.mariobros.sprites.tiles.InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
