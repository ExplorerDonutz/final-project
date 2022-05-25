package com.quickwoo.finalproject.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
    private final TiledMap map;
    public Map(TiledMap map, World world) {
        this.map = map;

        TiledObjectCollision.parseTiledObjectLayer(world, map.getLayers().get("collision").getObjects());
    }

    public TiledMap getMap() {
        return map;
    }
}
