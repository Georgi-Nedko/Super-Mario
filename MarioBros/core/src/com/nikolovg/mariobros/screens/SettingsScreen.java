
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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    TextButton backButton;
    Skin backButtonSkin;
    //SelectBox selectBox;
    //Skin selectBoxSkin;
    //  Skin listSkin;
    //List list;

    MarioBros game;
    Skin sliderSkin;
    TextureAtlas sliderAtlas;

    BitmapFont font;
    Stage stage;
    private Texture background;
    private OrthographicCamera cam;
    private Viewport viewport;
    private SettingsScreen settingsScreen;


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
        viewport = new FillViewport(800,400,cam);

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
        sliderAtlas = new TextureAtlas("skin-comic/comic-ui.atlas");
        sliderSkin = new Skin();
        sliderSkin.addRegions(sliderAtlas);
        sliderStyle.background = sliderSkin.getDrawable("slider");
        sliderStyle.knob = sliderSkin.getDrawable("slider-knob");

        //set height,width and position to slider
        slider = new Slider(1.0f,3.0f,0.1f,false,sliderStyle);
        slider.setHeight(stage.getViewport().getWorldHeight()/10f);
        slider.setWidth(stage.getViewport().getWorldWidth()/6f);
        slider.setPosition(stage.getViewport().getWorldWidth()/8f,stage.getViewport().getWorldHeight()/2f);

        //back button creating syle
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        backButtonSkin = new Skin();
        backButtonSkin.addRegions(sliderAtlas);
        textButtonStyle.font = font;
        textButtonStyle.font.getData().scale(0.3f);

        textButtonStyle.up = backButtonSkin.getDrawable("button");
        textButtonStyle.down = backButtonSkin.getDrawable("button-pressed");
        //backButton create,set height,width and set position
        backButton = new TextButton("Back",textButtonStyle);
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





        /**
         * a list if we need in the future
         *
        ///list style and LIST
        List.ListStyle listStyle  = new List.ListStyle();
        listSkin = new Skin();
        listSkin.addRegions(sliderAtlas);
        listStyle.font = font;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.selection = listSkin.getDrawable("checkbox");
        listStyle.background = listSkin.getDrawable("select-box-list");

        list = new List(listStyle);
        list.setWidth(stage.getViewport().getWorldWidth()/5f);
        list.setHeight(stage.getViewport().getWorldHeight()/5f);
        list.setPosition(stage.getViewport().getWorldWidth()/1.5f,stage.getViewport().getWorldHeight()/2f);

        /// LIST CREATED
        */


        /**
         * Select box if we need it in the future
         *
        ///select box
//        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
//        selectBoxSkin = new Skin();
//        selectBoxSkin.addRegions(sliderAtlas);
//        selectBoxStyle.background = selectBoxSkin.getDrawable("select-box");
//        selectBoxStyle.backgroundOpen = selectBoxSkin.getDrawable("select-box-open");
//
//        selectBoxStyle.font = font;
//        selectBoxStyle.fontColor = Color.WHITE;
//        selectBoxStyle.listStyle = listStyle;
//        selectBox = new SelectBox(selectBoxStyle);
//        selectBox.setWidth(stage.getViewport().getWorldWidth()/10f);
//        selectBox.setHeight(stage.getViewport().getWorldHeight()/5f);
//        selectBox.setPosition(stage.getViewport().getWorldWidth()/1.5f,stage.getViewport().getWorldHeight()/1.5f);

         **/


        stage.addActor(settingsLabel);
        stage.addActor(volumeLabel);
        stage.addActor(slider);
        stage.addActor(backButton);



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

    }
}
