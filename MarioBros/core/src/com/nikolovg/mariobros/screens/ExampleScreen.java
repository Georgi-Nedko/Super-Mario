package com.nikolovg.mariobros.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

/**
 * Created by svetlio on 24.8.2016 г..
 */
public class ExampleScreen implements Screen{
    Stage stage;
    TextButton button;
    MarioBros game;
    Skin buttonSkin;
    TextureAtlas buttonsAtlas;
    BitmapFont font;
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;

    public ExampleScreen(MarioBros g) {
        this.game = g;
        this.cam = new OrthographicCamera();

        cam.setToOrtho(false, MarioBros.V_WIDTH/2, MarioBros.V_HEIGHT/2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        background = new Texture("mario-screen.jpg");

        buttonsAtlas = new TextureAtlas("skin-comic/comic-ui.atlas"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//

        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false); //** font **//
        viewport = new FillViewport(800, 450, cam);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("button");
        style.down = buttonSkin.getDrawable("button-pressed");
        style.font = font;




        button = new TextButton("Play", style);
        button.setColor(Color.GREEN);
        font.getData().scale(0.4f);



        button.setHeight(stage.getViewport().getScreenHeight() / 15 ); //** Button Height **//
        button.setWidth(stage.getViewport().getScreenWidth() / 15); //** Button Width **//
        button.setPosition(stage.getViewport().getWorldWidth()/1.7f,stage.getViewport().getWorldHeight()/3.6f);
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LoadingScreen(game));
                dispose();
            }
        }
        );
        stage.addActor (button);




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(background,0,0);
        game.batch.end();

        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
