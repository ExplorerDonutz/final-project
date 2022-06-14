package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StateComponent implements Component, Pool.Poolable {
    public static final int STATE_UP = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_LEFT = 2;
    public static final int STATE_RIGHT = 3;
    public static final int STATE_ATTACK_UP = 4;
    public static final int STATE_ATTACK_DOWN = 5;
    public static final int STATE_ATTACK_LEFT = 6;
    public static final int STATE_ATTACK_RIGHT = 7;
    public static final int STATE_SLIME_RIGHT = 8;
    public static final int STATE_SLIME_LEFT = 9;
    public float time = 0.0f;
    public boolean isLooping = false;
    public int state = 0;

    public int getState() {
        return state;
    }

    public void setState(int newState){
        state = newState;
    }

    @Override
    public void reset() {
        state = 0;
        time = 0.0f;
        isLooping = true;
    }
}
