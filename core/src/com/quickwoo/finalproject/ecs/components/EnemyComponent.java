package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {
    public Entity player = null;
    public float speed = 0;
    public int coolDown;

    @Override
    public void reset() {
        player = null;
        coolDown = 0;
        speed = 0;
    }
}
