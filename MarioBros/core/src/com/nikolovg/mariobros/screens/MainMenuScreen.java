package com.nikolovg.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.Screen;




/**
 * Created by svetlio on 24.8.2016 г..
 */
public class MainMenuScreen implements Screen {
    private Stage stage;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton exitButton;
    private Label mainMenu;
    private MarioBros game;
    private Skin buttonSkin; //The Skin class stores resources for UI widgets to use
    private TextureAtlas buttonsAtlas; //The Texture Atlas output is a directory of page images and a text file that describes all the images packed on the pages//
    private BitmapFont font; //If you want to draw text in your game, you usually use a BitmapFont
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;  // how the screen looks//
    public static final Color myColor = new Color(1f,0.35f,0f,0.9f);
    public static final Color myColorAlphaChanged = new Color(1f,0.2f,0f,0.7f);




    public MainMenuScreen(MarioBros g) {
        //setting game
        this.game = g;
        //set camera
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        //set background
        background = new Texture("mario_mainMenu.jpg");

        //set viewport//
        viewport = new FillViewport(800, 450, cam);
        //set stage
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
        //create font //
        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        style.font = font;
        font.getData().scale(0.3f);


        //making new game button
        newGameButton = new TextButton("New Game",style);
        newGameButton.setColor(myColor);

        //set position and its dimensionality
        newGameButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        newGameButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        newGameButton.setPosition(stage.getViewport().getWorldWidth() / 1.8f, stage.getViewport().getWorldHeight() / 1.64f);
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
        //making label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        //main menu label
        mainMenu = new Label("MAIN MENU", labelStyle);
        mainMenu.setPosition(stage.getViewport().getWorldWidth()/1.91f,stage.getViewport().getWorldHeight()/1.27f);
        mainMenu.setFontScale(1.7f);


        //making Load Game button
        loadGameButton = new TextButton("Credits",style);
        loadGameButton.setColor(myColor);
        //set position and its dimensionality
        loadGameButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        loadGameButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        loadGameButton.setPosition(stage.getViewport().getWorldWidth() / 1.8f, stage.getViewport().getWorldHeight() / 2.18f);
        //listener for load game button
        loadGameButton.addListener(new InputListener() {
                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                          return true;
                                      }

                                      @Override
                                      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                          game.setScreen(new CreditsScreen(game));
                                          dispose();
                                      }
                                  }
        );

        //making settings button
        settingsButton = new TextButton("Settings",style);
        settingsButton.setColor(myColor);
        //set position and its dimensionality
        settingsButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        settingsButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        settingsButton.setPosition(stage.getViewport().getWorldWidth() / 1.8f, stage.getViewport().getWorldHeight() / 3.28f);
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
        exitButton.setColor(myColor);
        settingsButton.setColor(myColor);
        //set position and its dimensionality
        exitButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        exitButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        exitButton.setPosition(stage.getViewport().getWorldWidth() / 1.8f, stage.getViewport().getWorldHeight() / 6.38f);
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

        //add actors to the stage
        stage.addActor(mainMenu);
        stage.addActor(newGameButton);
        stage.addActor(loadGameButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);


    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.4f, 0.5f, 0.6f);
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
