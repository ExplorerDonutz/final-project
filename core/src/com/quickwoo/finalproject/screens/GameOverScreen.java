package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.loader.AssetLoader;

import static com.quickwoo.finalproject.Constants.HEIGHT;
import static com.quickwoo.finalproject.Constants.WIDTH;

public class GameOverScreen implements Screen {
    private final FinalProject game;
    private Stage stage;
    private FillViewport viewport;
    private boolean bossDefeated = false;

    public GameOverScreen(FinalProject game) {
        this.game = game;
    }

    @Override
    public void show() {
        OrthographicCamera cam = game.getCamera();
        viewport = new FillViewport(WIDTH, HEIGHT, cam);

        // Create a stage to act on
        stage = new Stage(viewport);
        Skin skin = game.getAssetManager().manager.get(AssetLoader.SKIN);

        // Let the stage control the game's input
        Gdx.input.setInputProcessor(stage);

        // Create a table to organize actors on
        Table table = new Table();
        table.setFillParent(true);

        if (bossDefeated) {
            Image image = new Image(skin, "win");
            table.add(image).size(300, 65.45f).fill().padBottom(10.0f);
        } else {
            Image image = new Image(skin, "lose");
            table.add(image).size(300, 65.45f).fill().padBottom(10.0f);
        }

        table.row();
        TextButton quitButton = new TextButton("Quit", skin);
        // Have the quit button detect mouse clicks on itself
        quitButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        }));
        table.add(quitButton).fillX();

        stage.addActor(table);

        // Fade in screen
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.75f)));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.512f, 0.137f, 0.598f, 0);
        viewport.apply(true);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    public void isBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
