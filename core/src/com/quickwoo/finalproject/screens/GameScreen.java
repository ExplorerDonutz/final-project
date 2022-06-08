/* Michael Quick & Nicholas Woo
 * 17 May 2022
 * The main screen for the game where all gameplay occurs
 */
package com.quickwoo.finalproject.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.audio.AudioType;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.HealthComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.map.Map;
import com.quickwoo.finalproject.map.MapManager;

import static com.quickwoo.finalproject.input.GameKeys.BACK;

public class GameScreen implements Screen, GameKeyInputListener, MapManager.MapListener {
    private final World world;
    private final ECSEngine ecsEngine;
    private final Skin skin;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private final ExtendViewport viewport;
    private final OrthographicCamera cam;
    private final AssetManager assetManager;
    private final com.quickwoo.finalproject.map.Map map;
    private final Stage stage;
    private final Window pause;
    private final MapManager mapManager;
    private FinalProject game;
    private boolean isPaused;

    public GameScreen(FinalProject game) {
        isPaused = false;
        this.game = game;
        assetManager = game.getAssetManager().manager;

        // Create skin, stage, and its menu window
        skin = assetManager.get(AssetLoader.SKIN);
        stage = new Stage(new ExtendViewport(Constants.WIDTH, Constants.HEIGHT), game.getBatch());
        pause = new Window("", skin);

        pause.setFillParent(true);

        pause.pack();
        pause.setMovable(false);
        TextButton resume = new TextButton("Resume", skin);
        resume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                pause.setVisible(false);
                isPaused = false;
            }
        });

        pause.add(resume);
        pause.row();

        TextButton menu = new TextButton("Main Menu", skin);
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                pause.setVisible(false);
                isPaused = true;
                game.getAudioManager().pauseMusic();
                    game.setScreen(ScreenType.MENU);
            }
        });

        pause.add(menu);

        stage.addActor(pause);
        pause.setVisible(false);

        // Create a new physics world with no gravity
        world = new World(Vector2.Zero, false);

        ecsEngine = new ECSEngine(world, game, assetManager, this, stage, skin);

        // Load tiled map, parse the collision layer, and create a renderer to render the map with the pixel to meter scale
        tiledMap = assetManager.get(AssetLoader.MAP_START);
        mapManager = new MapManager(world, ecsEngine);
        mapManager.addMapListener(this);
        map = new Map(tiledMap, world);
        mapRenderer = new OrthogonalTiledMapRenderer(null, Constants.PIXELS_TO_METERS, game.getBatch());
        mapManager.setMap(map);
        ecsEngine.getCameraSystem().setMap(map);

        cam = game.getCamera();
        viewport = new ExtendViewport(16, 9, cam);



        // Create entities
        ecsEngine.createPlayer(400, 400, 1);
        ecsEngine.createTest(400, 250, 2);


    }

    @Override
    public void show() {
        // Set both the input manager and stage as the input processor using an input multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(game.getInputManager());
        Gdx.input.setInputProcessor(multiplexer);
        game.getInputManager().addInputListener(this);

        // Ensure player's health-bar is visible
        Entity player = ecsEngine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        HealthComponent health = Mapper.healthMapper.get(player);
        health.healthBar.setVisible(true);

        // Play background music
        if (game.getAudioManager().isPaused)
            game.getAudioManager().resumeMusic();
        else
            game.getAudioManager().playAudio(AudioType.BACKGROUND);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CORAL);
        viewport.apply(false);

        if (!isPaused && mapRenderer.getMap() != null) {
            // Render map
            game.getBatch().setProjectionMatrix(cam.combined);
            mapRenderer.setView(cam);
            mapRenderer.render();

            // Update entities
            ecsEngine.update(delta);
        }

        // Render stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        game.getInputManager().removeInputListener(this);
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
        mapRenderer.dispose();
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == BACK && !isPaused) {
            isPaused = true;
            pause.setVisible(true);
            Entity player = ecsEngine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
            HealthComponent health = Mapper.healthMapper.get(player);
            health.healthBar.setVisible(false);
        } else if (key == BACK) {
            isPaused = false;
            pause.setVisible(false);
            Entity player = ecsEngine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
            HealthComponent health = Mapper.healthMapper.get(player);
            health.healthBar.setVisible(true);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void mapChanged(com.quickwoo.finalproject.map.Map map) {
        mapRenderer.setMap(map.getMap());
    }
}
