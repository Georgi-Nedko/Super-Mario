package com.nikolovg.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

/**
 * Created by Freeware Sys on 27.8.2016 г..
 */
public class GameOverScreen implements Screen {
    private  TextButton exitButton;
    private  TextButton settingsButton;
    private  Label gameOver;
    private Viewport viewport; // how the screen looks//
    private Stage stage;
    private MarioBros game;

    private TextButton newGameButton;
    private Skin buttonSkin; //The Skin class stores resources for UI widgets to use.
    private TextureAtlas buttonsAtlas; //The Texture Atlas output is a directory of page images and a text file that describes all the images packed on the pages//
    private  BitmapFont font; //If you want to draw text in your game, you usually use a BitmapFont
    private Texture background;
    private OrthographicCamera cam;

    public GameOverScreen(final MarioBros game){
        //setting game
        this.game = game;

        //set camera
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        //set background
        background = new Texture("mario-SettingsScreen.jpg");
        //set viewport//
        viewport = new FillViewport(800, 400, cam);
        //set stage//
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        //Make button style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); //** Button properties **//
        //get button looks from the atlas
        buttonsAtlas = new TextureAtlas("skin-comic/comic-ui.atlas"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        //** skins for on and off **//
        style.up = buttonSkin.getDrawable("button");
        style.down = buttonSkin.getDrawable("button-pressed");
        // font //
        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        style.font = font;
        font.getData().scale(0.3f);


        //making new game button
        newGameButton = new TextButton("New Game",style);
        newGameButton.setColor(0f,0f,0f,0.9f);

        //set position and its dimensionality
        newGameButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        newGameButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        newGameButton.setPosition(stage.getViewport().getWorldWidth() / 1.6f, stage.getViewport().getWorldHeight() / 1.8f);
        //set Listener
        newGameButton.addListener(new InputListener() {
                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                          return true;
                                      }

                                      @Override
                                      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                          game.setScreen(new SelectLevelScreen(game));
                                          dispose();
                                      }
                                  }
        );
        //making settings button
        settingsButton = new TextButton("Settings",style);
        settingsButton.setColor(0f,0f,0f,0.9f);

        //set position and its dimensionality
        settingsButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        settingsButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        settingsButton.setPosition(stage.getViewport().getWorldWidth() / 1.6f, stage.getViewport().getWorldHeight() / 2.2f);
        //listener for settings button
        settingsButton.addListener(new InputListener() {
                                       @Override
                                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                           return true;
                                       }

                                       @Override
                                       public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                           game.setScreen(new SettingsScreen(game));
                                           dispose();
                                       }
                                   }
        );

        //making exit button
        exitButton = new TextButton("Exit",style);
        exitButton.setColor(0f,0f,0f,0.9f);

        //set position and its dimensionality
        exitButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        exitButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        exitButton.setPosition(stage.getViewport().getWorldWidth() / 1.6f, stage.getViewport().getWorldHeight() / 2.9f);
        //listener for exit button
        exitButton.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                       // game.setScreen(new SelectLevelScreen(game));
                                       System.exit(0);
                                       dispose();
                                   }
                               }
        );


        //making label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        //game over  label
        gameOver = new Label("GAME OVER", labelStyle);
        gameOver.setPosition(stage.getViewport().getWorldWidth()/4.4f,stage.getViewport().getWorldHeight()/1.3f);
        gameOver.setFontScale(2.7f);

        //add actors to stage
        stage.addActor(newGameButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
        stage.addActor(gameOver);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        //the batch draw the background
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(background,0,0);
        game.batch.end();

        //stage draw actors
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
        background.dispose();
        stage.dispose();
    }
}
