package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.quickwoo.finalproject.screens.HeartBar;

public class HealthComponent implements Component, Pool.Poolable {

    public int health = 3;
    public HeartBar healthBar;
    @Override
    public void reset() {
        health = 3;
        healthBar = null;
    }
}
