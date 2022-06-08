package com.quickwoo.finalproject.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {
    private final TiledMap map;
    private final Array<GameObject> gameObjects;
    private final Array<Body> bodies;

    public Map(TiledMap map, World world) {
        this.map = map;
        gameObjects = new Array<>();
        final MapLayer gameObjectsLayer = map.getLayers().get("gameObjects");

        final MapObjects objects = gameObjectsLayer.getObjects();
        for (final MapObject mapObj : objects) {
            final TiledMapTileMapObject tiledMapObj = (TiledMapTileMapObject) mapObj;
            final MapProperties tiledMapObjProperties = tiledMapObj.getProperties();
            final float width = tiledMapObjProperties.get("width", Float.class);
            final float height = tiledMapObjProperties.get("height", Float.class);
            final int type = tiledMapObjProperties.get("type", Integer.class);

            if (tiledMapObjProperties.get("nextMap", String.class) != null) {
                final String nextMap = tiledMapObjProperties.get("nextMap", String.class);
                gameObjects.add(new GameObject(type, tiledMapObj.getX(), tiledMapObj.getY(), width, height, tiledMapObj.getRotation(), nextMap, tiledMapObj.getTextureRegion()));
            } else {
                gameObjects.add(new GameObject(type, tiledMapObj.getX(), tiledMapObj.getY(), width, height, tiledMapObj.getRotation(), tiledMapObj.getTextureRegion()));
            }
        }

        bodies = TiledObjectCollision.parseTiledObjectLayer(world, map.getLayers().get("Collision").getObjects());
    }

    public Array<Body> getBodies() {
        return bodies;
    }

    public TiledMap getMap() {
        return map;
    }

    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }
}
