package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TextBoxComponent implements Component, Pool.Poolable{

    public String text = null;

    @Override
    public void reset() {
        text = null;
    }
}
