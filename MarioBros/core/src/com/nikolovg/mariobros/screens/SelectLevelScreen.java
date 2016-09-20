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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

import java.time.format.TextStyle;

import sun.rmi.runtime.Log;

/**
 * Created by Freeware Sys on 23.8.2016 г..
 */
public class SelectLevelScreen implements Screen {
    private MarioBros game;
    private OrthographicCamera cam;
    private Texture background;
    private Viewport viewport;
    private Stage stage;

    private TextButton level_1;
    private TextButton level_2;
    private TextButton level_test;
    private Skin buttonSkin;
    private TextureAtlas buttonsAtlas;
    private BitmapFont font;

    public SelectLevelScreen(final MarioBros game) {
        this.game = game;

        this.cam = new OrthographicCamera();

        cam.setToOrtho(false, MarioBros.V_WIDTH/2, MarioBros.V_HEIGHT/2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        viewport = new FillViewport(800, 420, cam);
        background = new Texture("loading.jpg");

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
        //** font **//ont **//
        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        style.font = font;
        font.getData().scale(0f);

        level_1 = createButton(level_1,style,"level1.tmx","level 1", "1-1");
       // level_test = createButton(level_test,style,"testLevel.tmx","Test");
       // level_test.setPosition(stage.getViewport().getWorldWidth()/3.6f,stage.getViewport().getWorldHeight()/1.3f);
        level_2 = createButton(level_2,style,"level2.tmx","level 2", "2-1");
        level_2.setPosition(stage.getViewport().getWorldWidth()/3.6f,stage.getViewport().getWorldHeight()/1.3f);

        stage.addActor(level_1);
        stage.addActor(level_2);

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


        stage.act();

        stage.draw();



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
        stage.dispose();
    }
    public TextButton createButton(TextButton button, TextButton.TextButtonStyle style, final String level,String name, final String hudWorld){


        //set button properties
        //level.split(".tmx")[0]
        button = new TextButton(name,style);
        // level_1.setColor(MainMenuScreen.myColor);
        button.setColor(MainMenuScreen.myColorAlphaChanged);



        final double alphaColor = button.getColor().a;
        //set height,width
        button.setHeight(stage.getViewport().getWorldHeight() / 8); //** Button Height **//
        button.setWidth(stage.getViewport().getWorldWidth() / 7.6f); //** Button Width **//
        //set position
        button.setPosition(stage.getViewport().getWorldWidth()/ 15f, stage.getViewport().getWorldHeight()/1.3f);
        //set Listener
        button.addListener(new InputListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    return true;
                                }

                                @Override
                                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                    if(alphaColor < 0.3f) {
                                        return;
                                    }
                                    else {

                                        game.setScreen(new PlayScreen(level, hudWorld, game));
                                        dispose();
                                    }
                                }
                            }
        );
        return button;
    }
}

