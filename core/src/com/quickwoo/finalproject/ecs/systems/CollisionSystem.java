package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.CollisionComponent;
import com.quickwoo.finalproject.ecs.components.GameObjectComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.map.Map;
import com.quickwoo.finalproject.map.MapManager;
import com.quickwoo.finalproject.screens.GameScreen;

public class CollisionSystem extends IteratingSystem {
private final MapManager mapManager;
private final World world;
    public CollisionSystem(MapManager mapManager, World world) {
        super(Family.one(PlayerComponent.class).get());
        this.mapManager = mapManager;
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final CollisionComponent collision = Mapper.collisionMapper.get(entity);
        final Entity collidedEntity = collision.collisionEntity;

        if (collidedEntity != null) {
            final GameObjectComponent gameObjectComponent = Mapper.gameObjectMapper.get(collidedEntity);
            if (gameObjectComponent != null) {
                switch (gameObjectComponent.getType()) {
                    case GameObjectComponent.TYPE_TELEPORT:
                        mapManager.setMap(new Map(gameObjectComponent.map, world));
                }
            }
        }
    }
}
