package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Pool;

public class GameObjectComponent implements Component, Pool.Poolable {
    public static final int TYPE_TELEPORT = 1;
    public static final int TYPE_SIGN = 2;
    public int type = 0;
    public String map;
    public int playerLoc;
    public boolean isInteractable = false;

    public int getType() {
        return type;
    }

    @Override
    public void reset() {
        type = 0;
        map = null;
        playerLoc = 1;
        isInteractable = false;
    }
}
