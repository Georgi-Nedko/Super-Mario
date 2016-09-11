package com.nikolovg.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

/**
 * Created by Freeware Sys on 23.8.2016 г..
 */
public class LoadingScreen implements Screen {
    private MarioBros game;
    private OrthographicCamera cam;
    private Texture background;
    private Viewport viewport;

    public LoadingScreen(final MarioBros game) {
        this.game = game;

        this.cam = new OrthographicCamera();

        cam.setToOrtho(false, MarioBros.V_WIDTH/2, MarioBros.V_HEIGHT/2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        viewport = new FillViewport(800, 480, cam);
        background = new Texture("loading.jpg");



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new PlayScreen("level2.tmx",game));
            dispose();
        }
    }

    @Override
    public void resize ( int width, int height){
        viewport.update(width,height);

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {
        background.dispose();
    }
}

