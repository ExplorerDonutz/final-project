package com.quickwoo.finalproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.MenuScreen;
import com.quickwoo.finalproject.screens.TileMaps;

import static com.quickwoo.finalproject.Constants.*;

public class FinalProject extends Game {
	public static final int MENU = 0;
	public static final int GAME = 1;

	// Set to true to get debug information
	public static final boolean DEBUG = true;

	private SpriteBatch batch;

	private InputManager inputManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		// Create camera and set position with scale
		TileMaps.createCam();

		// Create input manager that will be used to handle input through entire game
		inputManager = new InputManager();

		// Set the first screen
		this.setScreen(MENU);


	}
	@Override
	public void render () {
		super.render();
	}


	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return TileMaps.cam;
	}

	public void setScreen(int screen) {
		switch (screen) {
			case MENU:
				this.setScreen(new MenuScreen(this));
				break;
			case GAME:
				this.setScreen(new GameScreen(this));

		}
	}

	public InputManager getInputManager() {
		return inputManager;
	}
}
