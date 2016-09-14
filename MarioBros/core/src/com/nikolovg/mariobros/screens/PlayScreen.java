package com.nikolovg.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.scenes.Hud;
import com.nikolovg.mariobros.sprites.enemies.Enemy;
import com.nikolovg.mariobros.sprites.Mario;
import com.nikolovg.mariobros.sprites.items.Item;
import com.nikolovg.mariobros.sprites.items.ItemDef;
import com.nikolovg.mariobros.sprites.items.Mushroom;
import com.nikolovg.mariobros.tools.B2WorldCreator;
import com.nikolovg.mariobros.tools.Controller;
import com.nikolovg.mariobros.tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import sun.rmi.runtime.Log;

/**
 * Created by Freeware Sys on 8/18/2016.
 */
public class PlayScreen implements Screen{
    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Controller controller;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Mario player;

    private Music music;
    private TextureAtlas atlas;

    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    public PlayScreen(String level, MarioBros game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM,gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(level);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        controller = new Controller(game);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();


        player = new Mario(this, game);


        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();
        music.setVolume(SettingsScreen.volumeValue);

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();

    }

    public void spawnItem(ItemDef iDef){
        itemsToSpawn.add(iDef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef iDef = itemsToSpawn.poll();
            if(iDef.type == Mushroom.class){
                items.add(new Mushroom(this, iDef.position.x, iDef.position.y));
            }
        }
    }

    public void handleInput(float dt){
        // the player should not be able to control mario once he is dead
        if(player.currentState != Mario.State.DEAD) {

            if (((Gdx.input.isKeyJustPressed(Input.Keys.UP) || controller.isUpPressed())) && player.b2body.getLinearVelocity().y == 0 ) {//&& WorldContactListener.isJumpAllowed
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();
        world.step(1/60f, 6, 2);
        player.update(dt);

        for(Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224/ MarioBros.PPM){
                enemy.b2Body.setActive(true);
            }
        }

        for(Item item : items){
            item.update(dt);
        }

        hud.update(dt);

        // don't move the camera if mario happens to die while moving on the x axis
        if(player.currentState != Mario.State.DEAD) {
            gameCam.position.x = player.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);

    }


    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
        }


        for(Item item : items){
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        // only draw control buttons if mario is not dead
        if(player.currentState != Mario.State.DEAD) {
            controller.draw();
        }

        if(isGameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(Mario.isFinished && player.getStateTimer() > 1){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }
    public boolean isGameOver(){
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
            return  true;
        }
        return false;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
