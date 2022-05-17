package com.quickwoo.finalproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.LoadingScreen;
import com.quickwoo.finalproject.screens.MenuScreen;

import static com.quickwoo.finalproject.Constants.*;

public class FinalProject extends Game {
	public static final int LOAD = 0;
	public static final int MENU = 1;
	public static final int GAME = 2;

	// Set to true to get debug information
	public static final boolean DEBUG = true;

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private InputManager inputManager;
	private AssetLoader assetManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		assetManager = new AssetLoader();

		// Create camera and set position with scale
		cam = new OrthographicCamera(WIDTH / PPM, HEIGHT / PPM);
		cam.position.set(WIDTH / PPM / SCALE, HEIGHT / PPM / SCALE, 0);
		cam.update();

		// Create input manager that will be used to handle input through entire game
		inputManager = new InputManager();

		// Set the first screen
		this.setScreen(LOAD);
	}
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.manager.dispose();
	}

	public void setScreen(int screen) {
		switch (screen) {
			case LOAD:
				this.setScreen(new LoadingScreen(this));
				break;
			case MENU:
				this.setScreen(new MenuScreen(this));
				break;
			case GAME:
				this.setScreen(new GameScreen(this));

		}
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public AssetLoader getAssetManager() {
		return assetManager;
	}

	public InputManager getInputManager() {
		return inputManager;
	}
}
