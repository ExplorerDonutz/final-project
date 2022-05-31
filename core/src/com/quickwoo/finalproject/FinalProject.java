/* Michael Quick & Nicholas Woo
*  17 May 2022
*  A Zelda-esque fighting game
 */

package com.quickwoo.finalproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.screens.ScreenType;

import java.util.EnumMap;

import static com.quickwoo.finalproject.Constants.*;

public class FinalProject extends Game {
	public final String TAG = this.getClass().getSimpleName();
	public static final int LOAD = 0;
	public static final int MENU = 1;
	public static final int GAME = 2;

	// Set to true to get debug information
	public static final boolean DEBUG = true;

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private InputManager inputManager;
	private AssetLoader assetManager;
	private EnumMap<ScreenType, Screen> screenCache;

	@Override
	public void create () {
		// Create batch to draw with
		batch = new SpriteBatch();

		// Create asset loader for loading all the assets used in the game
		assetManager = new AssetLoader();

		// Create camera and set position and scale it to the appropriate size
		cam = new OrthographicCamera(WIDTH / PPM, HEIGHT / PPM);
		cam.position.set(WIDTH / PPM / SCALE, HEIGHT / PPM / SCALE, 0);
		cam.update();

		// Create input manager that will be used to handle input through entire game
		inputManager = new InputManager();

		// Set the first screen as the loading screen
		screenCache = new EnumMap<>(ScreenType.class);
		setScreen(ScreenType.LOADING);
	}
	@Override
	public void render () {
		// Render everything
		super.render();
	}
	
	@Override
	public void dispose () {
		// Dispose of disposable objects when the game is closed
		batch.dispose();
		assetManager.manager.dispose();
	}

	public void setScreen(ScreenType screenType) {
		// Get the last used screen, useful for returning to said screen
		screen = getScreen();

		final Screen screen = screenCache.get(screenType);

		if (screen == null) {
			// Create screen
			try {
				Gdx.app.log(TAG, "Creating " + screenType + " screen");
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), FinalProject.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException e) {
				throw new GdxRuntimeException("Screen " + screenType + " could not be created!", e);
			}
		} else {
			// Screen already exists or was created and can now be used
			setScreen(screen);
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
