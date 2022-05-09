package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StateComponent implements Component, Pool.Poolable {
    public static final int STATE_UP = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_LEFT = 2;
    public static final int STATE_RIGHT = 3;

    public float time = 0.0f;
    public boolean isLooping = false;
    public int state = 0;

    public int get() {
        return state;
    }

    public void set(int newState){
        state = newState;
        time = 0.0f;
    }

    @Override
    public void reset() {
        state = 0;
        time = 0.0f;
        isLooping = true;
    }
}
