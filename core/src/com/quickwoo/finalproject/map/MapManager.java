/* Michael Quick & Nick Woo
 * 27 May 2022
 * Handles the loading of maps and their objects
 */
package com.quickwoo.finalproject.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.ecs.ECSEngine;
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

    public void setMap(Map map) {

        // Destroy boundaries & game objects on old map
        if (currentMap != null) {
            for (Body b : new Array.ArrayIterator<>(map.getBodies())) {
                world.destroyBody(b);
            }

            ecsEngine.removeAllEntities(Family.all(GameObjectComponent.class).get());
        }

        currentMap = map;
        for (GameObject gameObject : map.getGameObjects()) {
            ecsEngine.createGameObject(gameObject);
        }

        // Inform listeners of map change
        for (final MapListener mapListener : new Array.ArrayIterator<>(mapListeners)) {
            mapListener.mapChanged(currentMap);
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public interface MapListener {
        void mapChanged(final Map map);
    }
}
