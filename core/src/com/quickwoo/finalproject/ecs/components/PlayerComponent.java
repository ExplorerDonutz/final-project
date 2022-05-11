package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {

    public float speed = 0;

    @Override
    public void reset() {
        speed = 0;
    }
}
