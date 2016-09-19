
package com.nikolovg.mariobros.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;
/**
 * Created by svetlio on 25.8.2016 г..
 */
public class SettingsScreen implements Screen {

    private Slider slider;
    private Label volumeLabel;
    private Label settingsLabel;
    private TextButton backButton;
    private Skin backButtonSkin;

    private CheckBox checkBoxOn;
    private CheckBox.CheckBoxStyle checkBoxStyle;

    private  MarioBros game;
    private  Skin sliderSkin;
    private TextureAtlas sliderAtlas;

    private BitmapFont font;
    private Stage stage;
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;
    private SettingsScreen settingsScreen;
    public static float volumeValue = 0.0f;


    public SettingsScreen(final MarioBros game){
        //setting game
        this.game = game;
        //setting cam
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2);
        cam.position.set(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, 0);
        //set backround
        background = new Texture("mario-SettingsScreen.jpg");

        //set viewport
        viewport = new FillViewport(background.getWidth(),background.getHeight(),cam);

        //set stage
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("skin/font-export.fnt"), false);
        //make Settings label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        //make Settings label and set position
        settingsLabel = new Label("Settings", labelStyle);
        settingsLabel.setPosition(stage.getViewport().getWorldWidth()/2.8f,stage.getViewport().getWorldHeight()/1.3f);
        settingsLabel.setFontScale(1.7f);

        // make Volume label and set position
        volumeLabel = new Label("Volume", labelStyle);
        volumeLabel.setPosition(stage.getViewport().getWorldWidth()/8f,stage.getViewport().getWorldHeight()/1.6f);
        volumeLabel.setFontScale(1.2f);

        //creating the  slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderAtlas = new TextureAtlas("skin-neon/neon-ui.atlas");
        sliderSkin = new Skin();
        sliderSkin.addRegions(sliderAtlas);
        sliderStyle.background = sliderSkin.getDrawable("slider");
        sliderStyle.knob = sliderSkin.getDrawable("slider-knob");

        //set height,width and position to slider

        slider = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        slider.setHeight(stage.getViewport().getWorldHeight()/10f);
        slider.setWidth(stage.getViewport().getWorldWidth()/6f);
        slider.setPosition(stage.getViewport().getWorldWidth()/8f,stage.getViewport().getWorldHeight()/2f);
        slider.setAnimateDuration(0.2f);
        slider.setVisible(true);
        slider.setValue(volumeValue);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                volumeValue = slider.getValue();
                Gdx.app.error("VOLUME value","" + volumeValue);
            }
        });

        //back button creating syle
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        backButtonSkin = new Skin();
        backButtonSkin.addRegions(sliderAtlas);
        textButtonStyle.font = font;
        textButtonStyle.font.getData().scale(0.2f);

        textButtonStyle.up = backButtonSkin.getDrawable("button");
        textButtonStyle.down = backButtonSkin.getDrawable("button-pressed");
        //backButton create,set height,width and set position
        backButton = new TextButton("Back to " + "\n" + " Main menu",textButtonStyle);
        backButton.setWidth(stage.getViewport().getWorldWidth()/5f);
        backButton.setHeight(stage.getViewport().getWorldHeight()/5f);
        backButton.setPosition(stage.getViewport().getWorldWidth()/12f,stage.getViewport().getWorldHeight()/12f);
        backButton.setColor(0f,0f,0f,0.9f);
        //back button set listener
        backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });









        stage.addActor(settingsLabel);
        stage.addActor(volumeLabel);
        stage.addActor(slider);
        stage.addActor(backButton);
       // stage.addActor(checkBoxOn);



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
        background.dispose();
        stage.dispose();
    }
}
