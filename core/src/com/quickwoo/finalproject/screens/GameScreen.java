/* Michael Quick & Nicholas Woo
 * 17 May 2022
 * The main screen for the game where all gameplay occurs
 */
package com.quickwoo.finalproject.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.map.Map;

import static com.quickwoo.finalproject.input.GameKeys.BACK;

public class GameScreen implements Screen, GameKeyInputListener {
    private final World world;
    private final ECSEngine ecsEngine;
    private final Skin skin;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final ExtendViewport viewport;
    private final OrthographicCamera cam;
    private final AssetManager assetManager;
    private final Map map;
    private final Stage stage;
    private final Window pause;
    private FinalProject game;
    private boolean isPaused;

    public GameScreen(FinalProject game) {
        isPaused = false;
        this.game = game;
        assetManager = game.getAssetManager().manager;

        // Create skin, stage, and its menu window
        skin = assetManager.get(AssetLoader.SKIN);
        stage = new Stage(new FillViewport(Constants.WIDTH, Constants.HEIGHT), game.getBatch());
        pause = new Window("", skin);

        // Create a new physics world with no gravity
        world = new World(Vector2.Zero, false);

        // Load tiled map, parse the collision layer, and create a renderer to render the map with the pixel to meter scale
        tiledMap = assetManager.get(AssetLoader.MAP_1);
        map = new Map(tiledMap, world);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.PIXELS_TO_METERS, game.getBatch());


        // Set both the input manager and stage as the input processor using an input multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(game.getInputManager());
        Gdx.input.setInputProcessor(multiplexer);
        game.getInputManager().addInputListener(this);

        cam = game.getCamera();
        viewport = new ExtendViewport(16, 9, cam);

        ecsEngine = new ECSEngine(world, game, assetManager, this);
        ecsEngine.getCameraSystem().setMap(map);
    }

    @Override
    public void show() {

        // Create entities
        ecsEngine.createPlayer(400, 400, 1);
        ecsEngine.createTest(400, 250, 2);

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
                isPaused = false;
                game.setScreen(FinalProject.MENU);
            }
        });

        pause.add(menu);

        pause.pack();

        pause.setBounds((Constants.WIDTH - 400) / 2f, (Constants.HEIGHT - 400) / 2f, 400, 400);
        stage.addActor(pause);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CORAL);
        viewport.apply(false);

        if (!isPaused) {
            // Render map
            game.getBatch().setProjectionMatrix(cam.combined);
            mapRenderer.setView(cam);
            mapRenderer.render();

            // Update entities
            ecsEngine.update(delta);
        }

        if (isPaused) {
            // Render stage
            stage.act(delta);
            stage.draw();


        }
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
        pause();
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == BACK && !isPaused) {
            isPaused = true;
            pause.setVisible(true);
        } else if (key == BACK) {
            isPaused = false;
            pause.setVisible(false);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}
