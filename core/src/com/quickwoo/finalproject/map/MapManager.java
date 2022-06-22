/* Michael Quick & Nick Woo
 * 27 May 2022
 * Handles the loading of maps and their objects
 */
package com.quickwoo.finalproject.map;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.components.EnemyComponent;
import com.quickwoo.finalproject.ecs.components.GameObjectComponent;
import com.quickwoo.finalproject.loader.AssetLoader;

import java.util.EnumMap;

public class MapManager {
    private final Array<MapListener> mapListeners;
    private final World world;
    private final ECSEngine ecsEngine;
    private final EnumMap<Maps, Map> mapCache;
    private final AssetLoader assetManager;
    private Map currentMap;
    private Maps currentMapType;
    private Array<Body> bodies;
    private boolean newMap = false;

    public MapManager(World world, ECSEngine ecsEngine, AssetLoader assetManager) {
        this.world = world;
        this.ecsEngine = ecsEngine;
        this.assetManager = assetManager;
        mapListeners = new Array<>();
        bodies = new Array<>();
        mapCache = new EnumMap<>(Maps.class);
        currentMapType = null;
        currentMap = null;
    }

    public void setMap(Maps type, int playerLoc) {
        if (currentMapType == type) {
            // Already set
            return;
        } else {
            currentMapType = type;
        }

        // Destroy boundaries & game objects on old map
        if (currentMap != null) {
            for (Body b : new Array.ArrayIterator<>(bodies)) {
                world.destroyBody(b);
            }

            ecsEngine.removeAllEntities(Family.one(GameObjectComponent.class, EnemyComponent.class).get());
            newMap = false;
        }

        // Replace old map with the new map
        currentMap = mapCache.get(type);

        // This map doesn't exist in the cache, it needs to be created
        if (currentMap == null) {
            TiledMap tiledMap = assetManager.manager.get(type.getFilepath(), TiledMap.class);
            currentMap = new Map(tiledMap, world, ecsEngine);
            mapCache.put(type, currentMap);
            newMap = true;
        }


        // Create game objects from new map in the entity system
        for (GameObject object : new Array.ArrayIterator<>(currentMap.getGameObjects())) {
            ecsEngine.createGameObject(object);
        }

        // Get the number of enemies and their starting positions on the map IF the map has never been entered before
        if (newMap) {
            for (MapObject object : currentMap.getMap().getLayers().get("enemyStart").getObjects()) {
                ecsEngine.createSlime((int) object.getProperties().get("x", Float.class).floatValue() * 2, (int) object.getProperties().get("y", Float.class).floatValue() * 2, 1);
                currentMap.setEnemyCount(currentMap.getEnemyCount() + 1);
            }
        }

        // Get the players starting location on the map
        for (MapObject object : currentMap.getMap().getLayers().get("playerStart").getObjects()) {
            if (object.getProperties().get("playerLoc", Integer.class) == playerLoc) {
                currentMap.setPlayerStartLocation(new Vector2(object.getProperties().get("x", Float.class) / Constants.PPM, object.getProperties().get("y", Float.class) / Constants.PPM));
                break;
            }
        }

        if (currentMap.isBossMap()) {
            for (MapObject object : currentMap.getMap().getLayers().get("bossStart").getObjects()) {
                ecsEngine.createBoss((int) object.getProperties().get("x", Float.class).floatValue() * 2, (int) object.getProperties().get("y", Float.class).floatValue() * 2, 1);
                break;
            }
        }

        bodies = TiledObjectCollision.parseTiledObjectLayer(world, currentMap.getMap().getLayers().get("Collision").getObjects());

        // Inform listeners of map change
        for (final MapListener mapListener : new Array.ArrayIterator<>(mapListeners)) {
            mapListener.mapChanged(currentMap, currentMap.isBossMap());
        }
    }


    public Map getCurrentMap() {
        return currentMap;
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public interface MapListener {
        void mapChanged(final Map map, boolean isBossMap);
    }
}
