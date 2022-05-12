package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.ECSEngine;

public class GameScreen implements Screen {
    private final World world;
    private final ECSEngine ecsEngine;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera cam;
    private FinalProject game;

    public GameScreen(FinalProject game) {
        this.game = game;

        // Set input manager as the input processor
        Gdx.input.setInputProcessor(game.getInputManager());

        // Create a new physics world with no gravity
        world = new World(Vector2.Zero, false);
        ecsEngine = new ECSEngine(world, game, this);
        tiledMap = new TmxMapLoader().load("Map/GrassPLains1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.PIXELS_TO_METERS, game.getBatch());

        cam = game.getCamera();
    }

    @Override
    public void show() {

        ecsEngine.createPlayer(100, 100, 1);
        ecsEngine.createTest(200, 200, 2);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CORAL);

        // Render map
        game.getBatch().setProjectionMatrix(cam.combined);
        mapRenderer.setView(cam);
        mapRenderer.render();

        // Update entities
        ecsEngine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        cam.update();
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
        world.dispose();
    }
}
