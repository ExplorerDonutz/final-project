package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BattleComponent implements Component, Pool.Poolable {
    int attack = 0;
    int defend = 0;
    int boost = 0;

    @Override
    public void reset() {
        attack = 0;
        defend = 0;
        boost = 0;
    }
}
