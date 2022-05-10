package com.quickwoo.finalproject.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private final GameKeys[] keyMapping;
    private final boolean[] keyState;
    private final Array<GameKeyInputListener> listeners;

    public InputManager() {
        this.keyMapping = new GameKeys[256];
        for (final GameKeys gameKeys : GameKeys.values()) {
            for (final int code : gameKeys.keyCode) {
                keyMapping[code] = gameKeys;
            }
        }

        keyState = new boolean[GameKeys.values().length];
        listeners = new Array<>();
    }

    public void addInputListener(final GameKeyInputListener listener) {
        listeners.add(listener);
    }

    public void removeInputListener(final GameKeyInputListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            // no mapping --> nothing to do
            return false;
        }

        notifyKeyDown(gameKey);

        return false;
    }

    public void notifyKeyDown(GameKeys gameKey) {
        keyState[gameKey.ordinal()] = true;
        for (final GameKeyInputListener listener : new Array.ArrayIterator<>(listeners)) {
            listener.keyPressed(this, gameKey);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            // no mapping --> nothing to do
            return false;
        }

        notifyKeyUp(gameKey);

        return false;
    }

    private void notifyKeyUp(GameKeys gameKey) {
        keyState[gameKey.ordinal()] = false;
        for (final GameKeyInputListener listener : new Array.ArrayIterator<>(listeners)) {
            listener.keyUp(this, gameKey);
        }
    }

    public boolean isKeyPressed(final GameKeys gameKey) {
        return keyState[gameKey.ordinal()];
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
