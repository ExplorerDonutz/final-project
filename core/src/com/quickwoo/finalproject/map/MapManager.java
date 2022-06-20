/* Michael Quick & Nick Woo
 * 27 May 2022
 * Handles the loading of maps and their objects
 */
package com.quickwoo.finalproject.map;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.components.EnemyComponent;
import com.quickwoo.finalproject.ecs.components.GameObjectComponent;

public class MapManager {
    private final Array<MapListener> mapListeners;
    private Map currentMap;
    private final World world;
    private final ECSEngine ecsEngine;

    public MapManager(World world, ECSEngine ecsEngine) {
        this.world = world;
        this.ecsEngine = ecsEngine;
        mapListeners = new Array<>();
    }

    public void setMap(Map map, int playerLoc) {

        // Destroy boundaries & game objects on old map
        if (currentMap != null) {
            for (Body b : new Array.ArrayIterator<>(currentMap.getBodies())) {
                world.destroyBody(b);
            }

            ecsEngine.removeAllEntities(Family.one(GameObjectComponent.class, EnemyComponent.class).get());
        }

        // Replace old map with the new map
        currentMap = map;


        // Create game objects from new map in the entity system
        for (GameObject gameObject : new Array.ArrayIterator<>(map.getGameObjects())) {
            ecsEngine.createGameObject(gameObject);
        }

        // Get the number of enemies and their starting positions on the map
        for (MapObject object : map.getMap().getLayers().get("enemyStart").getObjects()) {
            ecsEngine.createSlime((int) object.getProperties().get("x", Float.class).floatValue() * 2, (int) object.getProperties().get("y", Float.class).floatValue() * 2, 1);
            currentMap.setEnemyCount(currentMap.getEnemyCount() + 1);
        }

        // Get the players starting location on the map
        for (MapObject object : map.getMap().getLayers().get("playerStart").getObjects()) {
            if (object.getProperties().get("playerLoc", Integer.class) == playerLoc) {
                map.setPlayerStartLocation(new Vector2(object.getProperties().get("x", Float.class) / Constants.PPM, object.getProperties().get("y", Float.class) / Constants.PPM));
                break;
            }
        }

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
