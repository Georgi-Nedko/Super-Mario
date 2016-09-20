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

import java.util.concurrent.LinkedBlockingDeque;

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
    private boolean isMoved;
    private String level;

    private static final int RADIUS_OF_MONSTER_SLEEP = 224;
    private static final float DEFAULT_WAIT_FOR_TRANSITION = 3;
    private static final float TIME_BEFORE_SPRITE_REMOVAL = 0.5f;
    private static final float IMPULSE_TO_CASTLE_DOOR_ON_LVL1 = 2.25f;
    private  static final float IMPULSE_TO_CASTLE_DOOR_ON_LVL2 = 2.1f;
    private static final float RUNNING_IMPULSE_LEFT = -0.1f;
    private static final float RUNNING_IMPULSE_RIGHT = 0.1f;
    private static final float IMPULSE_JUMP = 4f;
    private static final float WORLD_GRAVITY = -10;

    public PlayScreen(String level, String hudWorld, MarioBros game){
        isMoved = false;
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM,gameCam);
        hud = new Hud(hudWorld,game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(level);
        this.level = level;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        controller = new Controller(game);

        world = new World(new Vector2(0, WORLD_GRAVITY), true);
        b2dr = new Box2DDebugRenderer();


        player = new Mario(this, game);


        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());

        music = game.manager.get("audio/music/mario_music.ogg", Music.class);
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
        if(player.currentState != Mario.State.DEAD && !Mario.isFinished) {

            if (((Gdx.input.isKeyJustPressed(Input.Keys.UP) || controller.isUpPressed())) && player.b2body.getLinearVelocity().y == 0 ) {//&& WorldContactListener.isJumpAllowed
                player.b2body.applyLinearImpulse(new Vector2(0, IMPULSE_JUMP), player.b2body.getWorldCenter(), true);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(RUNNING_IMPULSE_RIGHT, 0), player.b2body.getWorldCenter(), true);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(RUNNING_IMPULSE_LEFT, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }
    public void moveMarioToCastle(float dt){
        if(this.level.equals("level1.tmx")){
            player.b2body.applyLinearImpulse(new Vector2(IMPULSE_TO_CASTLE_DOOR_ON_LVL1, 0), player.b2body.getWorldCenter(), true);
        }
        else{
            player.b2body.applyLinearImpulse(new Vector2(IMPULSE_TO_CASTLE_DOOR_ON_LVL2, 0), player.b2body.getWorldCenter(), true);
        }

        isMoved = true;
    }

    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();
        world.step(1/60f, 6, 2);
        player.update(dt);
        if(Mario.isFinished && !isMoved){
            if(player.getState() == Mario.State.STANDING)
                moveMarioToCastle(dt);
        }
        for(Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + RADIUS_OF_MONSTER_SLEEP/ MarioBros.PPM){
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
        //helps for checking collisions
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        // stop drawing mario when he is moved to the castle door and is there for more than 0.5 seconds
        if(!((isMoved && player.b2body.getLinearVelocity().x == 0 )&& player.getStateTimer() > TIME_BEFORE_SPRITE_REMOVAL )){
            player.draw(game.batch);
        }

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
        if(player.currentState != Mario.State.DEAD  && !Mario.isFinished) {
            controller.draw();
        }

        if(isGameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(Mario.isFinished && player.getStateTimer() > DEFAULT_WAIT_FOR_TRANSITION){
            music.stop();
            game.setScreen(new CompleteLevelScreen(game));
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
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > DEFAULT_WAIT_FOR_TRANSITION){
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

    public Hud getHud() {
        return hud;
    }
    public MarioBros getGame(){
        return this.game;
    }
}
