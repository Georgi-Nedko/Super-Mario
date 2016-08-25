
package com.nikolovg.mariobros.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;
/**
 * Created by svetlio on 25.8.2016 г..
 */
public class SettingsScreen implements Screen {

    Slider slider;
    Label volumeLabel;
    Label settingsLabel;
    MarioBros game;
    Skin sliderSkin;
    TextureAtlas sliderAtlas;
    BitmapFont font;
    Stage stage;
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;
    private SettingsScreen settingsScreen;

    public SettingsScreen(MarioBros game){
        //setting game
        this.game = game;
        //setting cam
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        //set backround
        background = new Texture("mario-SettingsScreen.jpg");

        //set viewport
        viewport = new FillViewport(800,400,cam);

        //set stage
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        //
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        settingsLabel = new Label("Settings", labelStyle);
        settingsLabel.setPosition(stage.getViewport().getWorldWidth()/2.8f,stage.getViewport().getWorldHeight()/1.3f);
        settingsLabel.setFontScale(1.7f);
        //
        volumeLabel = new Label("Volume", labelStyle);
        volumeLabel.setPosition(stage.getViewport().getWorldWidth()/8f,stage.getViewport().getWorldHeight()/1.6f);
        volumeLabel.setFontScale(1.2f);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderAtlas = new TextureAtlas("skin-comic/comic-ui.atlas");
        sliderSkin = new Skin();
        sliderSkin.addRegions(sliderAtlas);
        sliderStyle.background = sliderSkin.getDrawable("slider");
        sliderStyle.knob = sliderSkin.getDrawable("slider-knob");

        slider = new Slider(1.0f,3.0f,0.1f,false,sliderStyle);
       // slider.setRotation(180f);
        //slider.rotateBy(90f);
        slider.setHeight(stage.getViewport().getWorldHeight()/10f);
        slider.setWidth(stage.getViewport().getWorldWidth()/6f);

        //slider.setColor(Color.WHITE);
        slider.setPosition(stage.getViewport().getWorldWidth()/8f,stage.getViewport().getWorldHeight()/2f);




        stage.addActor(settingsLabel);
        stage.addActor(volumeLabel);
        stage.addActor(slider);


    }




    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0.4f, 0.5f, 0.6f);
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
    public void dispose()
    {
        //background.dispose();
        //buttonsAtlas.dispose();
       // buttonSkin.dispose();

    }
}
