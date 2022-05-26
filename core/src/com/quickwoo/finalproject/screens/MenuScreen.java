/* Michael Quick & Nicholas Woo
*  17 May 2022
*  The menu screen for the game
 */

package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.loader.AssetLoader;

public class MenuScreen implements Screen {
    private final FinalProject game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(FinalProject game) {
        this.game = game;
    }

    @Override
    public void show() {

        // Create a stage to act on
        stage = new Stage(new ScreenViewport());
        skin = game.getAssetManager().manager.get(AssetLoader.SKIN);

        // Let the stage control the game's input
        Gdx.input.setInputProcessor(stage);

        // Create a table to organize actors on
        Table table = new Table();
        table.setFillParent(true);

        Image image = new Image(skin, "Untitled");
        table.add(image).padBottom(10.0f).fill();
        table.row();
        TextButton playButton = new TextButton("Play", skin);
        // Have the play button detect mouse clicks on itself
        playButton.addListener((new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(FinalProject.GAME);
            }
        }));
        table.add(playButton).padBottom(10.0f).fillX();

        table.row();
        TextButton quitButton = new TextButton("Quit", skin);
        // Have the quit button detect mouse clicks on itself
        quitButton.addListener((new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        }));
        table.add(quitButton).fillX();
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
    }
}
