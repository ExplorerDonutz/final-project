package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

public class TutorialScreen implements Screen {
    private final FinalProject game;
    private Stage stage;
    private Skin skin;
    private OrthographicCamera cam;
    private FillViewport viewport;
    private Table table;

    public TutorialScreen(FinalProject game) {
        this.game = game;
    }

    @Override
    public void show() {
        cam = game.getCamera();
        viewport = new FillViewport(WIDTH, HEIGHT, cam);

        // Create a stage to act on
        stage = new Stage(viewport);
        skin = game.getAssetManager().manager.get(AssetLoader.SKIN);

        // Let the stage control the game's input
        Gdx.input.setInputProcessor(stage);

        // Create a table to organize actors on
        table = new Table();
        table.setFillParent(true);

        Label text1 = new Label("WASD to move", skin);
        Label text2 = new Label("Space to attack", skin);
        Label text3 = new Label("E to interact", skin);
        Label text4 = new Label("Beat the final boss to win", skin);

        TextButton backButton = new TextButton("Back", skin);
        // Have the play button detect mouse clicks on itself
        backButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                // Fade out then switch screens
                stage.addAction(Actions.sequence(Actions.fadeOut(0.75f), Actions.run(() -> game.setScreen(ScreenType.MENU))));
            }
        }));

        table.row();

        table.add(text1);
        table.row();
        table.add(text2);
        table.row();
        table.add(text3);
        table.row();
        table.add(text4);
        table.row();
        table.add(backButton);
        stage.addActor(table);
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
