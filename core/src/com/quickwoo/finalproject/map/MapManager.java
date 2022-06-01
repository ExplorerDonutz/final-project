/* Michael Quick & Nick Woo
 * 27 May 2022
 * Handles the loading of maps and their objects
 */
package com.quickwoo.finalproject.map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MapManager {
    private final Array<MapListener> mapListeners;
    private Map currentMap;
    private final World world;

    public MapManager(World world) {
        this.world = world;
        mapListeners = new Array<>();
    }

    public void setMap(Map map) {

        // Destroy boundaries on old map
        if (currentMap != null) {
            for (Body b : new Array.ArrayIterator<>(map.getBodies())) {
                world.destroyBody(b);
            }
        }

        currentMap = map;

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
