package com.nikolovg.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;

/**
 * Created by Freeware Sys on 22.8.2016 г..
 */
public class Controller {

    Viewport viewport;
    Stage stage;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    OrthographicCamera cam;
    MarioBros game;

    public Controller (MarioBros game){
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Table leftRightTable = new Table();
        leftRightTable.left().bottom();
        Table upTable = new Table();
        upTable.right().bottom();
        Image leftImg = new Image(new Texture("leftButton.png"));
        leftImg.setSize(75,75);
        leftImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               leftPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("rightButton.png"));
        rightImg.setSize(75,75);
        rightImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image upImg = new Image(new Texture("upButton.png"));
        upImg.setSize(75,75);


        upImg.addListener(new ClickListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        leftRightTable.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        leftRightTable.add().padRight(50);
        leftRightTable.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        leftRightTable.add().padRight(480);
        leftRightTable.add(upImg).size(upImg.getWidth(), upImg.getHeight());

        stage.addActor(leftRightTable);
    }


    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width,height);
    }
}
