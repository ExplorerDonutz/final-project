package com.quickwoo.finalproject.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {
    private final TiledMap map;
    private final Array<Body> bodies;
    public Map(TiledMap map, World world) {
        this.map = map;

        bodies = TiledObjectCollision.parseTiledObjectLayer(world, map.getLayers().get("collision").getObjects());
    }

    public Array<Body> getBodies() {
        return bodies;
    }

    public TiledMap getMap() {
        return map;
    }
}
