package com.quickwoo.finalproject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMap extends ApplicationAdapter {

    private TiledMap tiledMap;

    public TileMap() {

    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("GrassPLains1.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap);
    }
}
