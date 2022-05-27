package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable {

    public int health = 100;
    public ProgressBar healthBar;
    @Override
    public void reset() {
        health = 100;
        healthBar = null;
    }
}
