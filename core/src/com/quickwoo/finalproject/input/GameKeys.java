package com.quickwoo.finalproject.input;

import com.badlogic.gdx.Input;

public enum GameKeys {
    UP(Input.Keys.W, Input.Keys.UP),
    DOWN(Input.Keys.S, Input.Keys.DOWN),
    LEFT(Input.Keys.A, Input.Keys.LEFT),
    RIGHT(Input.Keys.D, Input.Keys.RIGHT),
    SELECT(Input.Keys.F),
    BACK(Input.Keys.ESCAPE),
    GRAPHIC(Input.Keys.F11),
    SPACE(Input.Keys.SPACE);

    final int[] keyCode;

    GameKeys(final int... keyCode) {
        this.keyCode = keyCode;
    }

    public int[] getKeyCode() {
        return keyCode;
    }
}
