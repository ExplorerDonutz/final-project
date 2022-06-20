package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BattleComponent implements Component, Pool.Poolable {
    public boolean attack = false;
    public int defend = 0;
    public int boost = 0;

    @Override
    public void reset() {
        attack = false;
        defend = 0;
        boost = 0;
    }
}
