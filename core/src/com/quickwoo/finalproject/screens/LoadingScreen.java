/* Michael Quick & Nicholas Woo
 * 21 June 2022
 * Screen used to visualize the loading for the user
 */
package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.loader.AssetLoader;

public class LoadingScreen implements Screen {
    private final AssetLoader assetManager;
    private final OrthographicCamera cam;
    private final FitViewport viewport;
    private final FinalProject game;
    private final Skin skin;
    private Stage stage;
    ProgressBar progressBar;

    public LoadingScreen(FinalProject game) {
        this.game = game;
        assetManager = game.getAssetManager();
        cam = game.getCamera();
        viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, cam);

        // Load skin for use in screen
        assetManager.queueLoading();
        assetManager.manager.finishLoading();
        skin = assetManager.manager.get(AssetLoader.SKIN);

        // Add the game assets to the loading queue
        assetManager.queueAssets();
    }

    @Override
    public void show() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        // Add text
        Label label = new Label("Loading...", skin);
        table.add(label);

        table.row();
        progressBar = new ProgressBar(0.0f, 100.0f, 1.0f, false, skin);
        table.add(progressBar);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // If the assetmanager finished loading, change screens
        if (assetManager.manager.update()) {
            assetManager.manager.finishLoading();
            stage.addAction(Actions.sequence(Actions.fadeOut(1), Actions.run(() -> {
                game.setScreen(ScreenType.MENU);
                dispose();
            })));
        }

        // Set the value of the progress bar to the progress of the assets being loaded
        progressBar.setValue(assetManager.manager.getProgress() * 100);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        stage.dispose();
    }
}
