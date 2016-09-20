package com.nikolovg.mariobros.screens;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

/**
 * Created by svetlio on 19.9.2016 г..
 */
public class CreditsScreen implements Screen{
    private MarioBros game;
    private OrthographicCamera cam;
    private Viewport viewport; // how the screen looks//
    private Texture background;
    private Stage stage;

    private Skin buttonSkin; //The Skin class stores resources for UI widgets to use.
    private TextureAtlas buttonsAtlas; //The Texture Atlas output is a directory of page images and a text file that describes all the images packed on the pages//
    private BitmapFont font;  //If you want to draw text in your game, you usually use a BitmapFont
    private TextButton backButton;

    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    public CreditsScreen(MarioBros g) {
        //setting game
        this.game = g;
        //set camera
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        //set background
        background = new Texture("mario-credits.jpg");
        //set viewport//
        viewport = new FillViewport(background.getWidth(), background.getHeight(), cam);
        //set stage//
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        //Make buttons style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        //get button looks from the atlas
        buttonsAtlas = new TextureAtlas("skin-comic/comic-ui.atlas"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        //** skins for up and down **//
        style.up = buttonSkin.getDrawable("button");
        style.down = buttonSkin.getDrawable("button-pressed");
        //** font **//
        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        style.font = font;
        font.getData().scale(0.3f);
        style.fontColor = Color.GOLDENROD;




        //making back button
        backButton = new TextButton("Back",style);
        backButton.setColor(0f,0f,0f,0.9f);
        //set position and its dimensionality
        backButton.setHeight(stage.getViewport().getWorldHeight() / 6); //** Button Height **//
        backButton.setWidth(stage.getViewport().getWorldWidth() / 4); //** Button Width **//
        backButton.setPosition(stage.getViewport().getWorldWidth()/1.3f, stage.getViewport().getWorldHeight() / 6f);
        //set Listener
        backButton.addListener(new InputListener() {
                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                          return true;
                                      }

                                      @Override
                                      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                          game.setScreen(new MainMenuScreen(game));
                                          dispose();
                                      }
                                  }
        );

        //making  label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        // creating label 1
        label1 = new Label("The game is created", labelStyle);
        label1.setPosition(stage.getViewport().getWorldWidth()/2.05f,stage.getViewport().getWorldHeight()/1.44f);
        label1.setFontScale(0.95f);
        label1.setColor(Color.GOLDENROD);


        //creating label 2
        label2 = new Label("by Georgi Nikolov", labelStyle);
        label2.setPosition(stage.getViewport().getWorldWidth()/2.05f,stage.getViewport().getWorldHeight()/1.6f);
        label2.setFontScale(0.95f);
        label2.setColor(Color.GOLDENROD);


        //creating label 3
        label3 = new Label("and Nedko Brymchev.", labelStyle);
        label3.setPosition(stage.getViewport().getWorldWidth()/2.05f,stage.getViewport().getWorldHeight()/1.8f);
        label3.setFontScale(0.95f);
        label3.setColor(Color.GOLDENROD);




        //creating label 1
        label4 = new Label("Thanks for playing!", labelStyle);
        label4.setPosition(stage.getViewport().getWorldWidth()/2.05f,stage.getViewport().getWorldHeight()/2.3f);
        label4.setFontScale(0.95f);
        label4.setColor(Color.GOLDENROD);


        //add actors to the stage
        stage.addActor(backButton);
        stage.addActor(label1);
        stage.addActor(label2);
        stage.addActor(label3);
        stage.addActor(label4);
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
