package com.nikolovg.mariobros.sprites.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.scenes.Hud;
import com.nikolovg.mariobros.screens.PlayScreen;
import com.nikolovg.mariobros.screens.SettingsScreen;
import com.nikolovg.mariobros.sprites.Mario;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class Brick extends com.nikolovg.mariobros.sprites.tiles.InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.getIsMarioBig()) {
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            screen.getGame().manager.get("audio/sounds/breakblock.wav", Sound.class).play(SettingsScreen.volumeValue);
        }
        else{
            screen.getGame().manager.get("audio/sounds/bump.wav", Sound.class).play(SettingsScreen.volumeValue);
        }
    }
}
