package com.quickwoo.finalproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.MenuScreen;

public class FinalProject extends Game {
	public static final int MENU = 0;
	public static final int GAME = 1;

	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

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

	public void setScreen(int screen) {
		switch (screen) {
			case MENU:
				this.setScreen(new MenuScreen(this));
				break;
			case GAME:
				this.setScreen(new GameScreen());

		}
	}
}
