package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quickwoo.finalproject.FinalProject;

public class MenuScreen implements Screen {
    private final FinalProject game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;

    public MenuScreen(FinalProject game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin.json"));
        atlas = new TextureAtlas("skin.atlas");
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Image image = new Image(skin, "Untitled");
        table.add(image);

        table.row();
        TextButton textButton = new TextButton("Play", skin);
        textButton.addListener((new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(FinalProject.GAME);
            }
        }));
        table.add(textButton).padBottom(10.0f).fillX();

        table.row();
        textButton = new TextButton("Quit", skin);
        textButton.addListener((new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        }));
        table.add(textButton).fillX();
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.SKY);
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
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }
}
