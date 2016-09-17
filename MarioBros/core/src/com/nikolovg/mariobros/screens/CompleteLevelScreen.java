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
 * Created by svetlio on 24.8.2016 г..
 */
public class CompleteLevelScreen implements Screen{
    private Stage stage;
    private TextButton newGameButton;
    private TextButton settingsButton;
    private TextButton exitButton;
    private  Label congrats;
    private MarioBros game;
    private Skin buttonSkin;
    private TextureAtlas buttonsAtlas;
    private BitmapFont font;
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;

    public CompleteLevelScreen(MarioBros g) {
        this.game = g;
        this.cam = new OrthographicCamera();

        cam.setToOrtho(false, MarioBros.V_WIDTH/2, MarioBros.V_HEIGHT/2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        background = new Texture("complete_level.jpg");

        buttonsAtlas = new TextureAtlas("skin-comic/comic-ui.atlas"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//

        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false); //** font **//
        viewport = new FillViewport(623, 350, cam);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("button");
        style.down = buttonSkin.getDrawable("button-pressed");
        style.font = font;

        font.getData().scale(0.1f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        //main menu label
        congrats = new Label("You are the best!", labelStyle);
        congrats.setPosition(stage.getViewport().getWorldWidth()/2.6f,stage.getViewport().getWorldHeight()/1.3f);
        congrats.setFontScale(1.3f);





        newGameButton = new TextButton("New game", style);
        newGameButton.setColor(1f,0.22f,0.3f,0.9f);

        newGameButton.setHeight(stage.getViewport().getWorldHeight() /6 ); //** Button Height **//
        newGameButton.setWidth(stage.getViewport().getWorldWidth() / 3.8f); //** Button Width **//
        newGameButton.setPosition(stage.getViewport().getWorldWidth()/1.5f,stage.getViewport().getWorldHeight()/1.8f);
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
        //settings button
        settingsButton = new TextButton("Settings",style);
        //settingsButton.setColor(MainMenuScreen.myColorAlphaChanged);
        settingsButton.setHeight(stage.getViewport().getWorldHeight() /6); //** Button Height **//
        settingsButton.setWidth(stage.getViewport().getWorldWidth() / 3.8f); //** Button Width **//
        settingsButton.setColor(1f,0.22f,0.3f,0.9f);
        //set position
        settingsButton.setPosition(stage.getViewport().getWorldWidth() / 1.5f, stage.getViewport().getWorldHeight() / 2.45f);
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

        //exit button
        exitButton = new TextButton("Exit",style);
        exitButton.setColor(1f,0.22f,0.3f,0.9f);
        exitButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        exitButton.setWidth(stage.getViewport().getWorldWidth() / 3.8f); //** Button Width **//
        //set position
        exitButton.setPosition(stage.getViewport().getWorldWidth() / 1.5f, stage.getViewport().getWorldHeight() / 3.75f);
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





        stage.addActor (newGameButton);
        stage.addActor (settingsButton);
        stage.addActor (exitButton);
        stage.addActor(congrats);




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
        background.dispose();
        stage.dispose();
    }
}
