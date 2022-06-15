/* Michael Quick & Nicholas Woo
 * 14 June 2022
 * System for all collisions involving the player
 */
package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.CollisionComponent;
import com.quickwoo.finalproject.ecs.components.GameObjectComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.map.Map;
import com.quickwoo.finalproject.map.MapManager;

public class CollisionSystem extends IteratingSystem {
    private final String TAG = this.getClass().getSimpleName();
    private MapManager mapManager;
    private final World world;

    public CollisionSystem(World world) {
        // Only iterate over entities with a player component
        super(Family.one(PlayerComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final CollisionComponent collision = Mapper.collisionMapper.get(entity);

        // Get the entity that the player collided into
        final Entity collidedEntity = collision.collisionEntity;

        // If the collided entity exists, determine what type it is
        if (collidedEntity != null) {
            final GameObjectComponent gameObjectComponent = Mapper.gameObjectMapper.get(collidedEntity);
            if (gameObjectComponent != null) {
                switch (gameObjectComponent.getType()) {
                    case GameObjectComponent.TYPE_TELEPORT:
                        Gdx.app.log(TAG, " Player hit teleport");
                        mapManager.setMap(new Map(gameObjectComponent.map, world));
                }
            }
        }
        // Reset the collision component of the player entity
        collision.reset();
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }
}
