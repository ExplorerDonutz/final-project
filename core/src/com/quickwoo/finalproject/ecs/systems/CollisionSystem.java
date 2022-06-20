/* Michael Quick & Nicholas Woo
 * 14 June 2022
 * System for all collisions involving the player
 */
package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.map.Map;
import com.quickwoo.finalproject.map.MapManager;

public class CollisionSystem extends IteratingSystem {
    private final String TAG = this.getClass().getSimpleName();
    private MapManager mapManager;
    private final World world;
    private final ECSEngine ecsEngine;

    public CollisionSystem(World world, ECSEngine ecsEngine) {
        // Only iterate over entities with a player component
        super(Family.one(PlayerComponent.class).get());
        this.world = world;
        this.ecsEngine = ecsEngine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final CollisionComponent collision = Mapper.collisionMapper.get(entity);

        // Get the entity that the player collided into
        final Entity collidedEntity = collision.collisionEntity;

        // If the collided entity exists, determine what type it is
        if (collidedEntity != null) {
            final GameObjectComponent gameObjectComponent = Mapper.gameObjectMapper.get(collidedEntity);
            final EnemyComponent enemyComponent = Mapper.enemyMapper.get(collidedEntity);
            if (gameObjectComponent != null) {
                switch (gameObjectComponent.getType()) {
                    case GameObjectComponent.TYPE_TELEPORT:
                        if (mapManager.getCurrentMap().getEnemyCount() == 0) {
                            Gdx.app.log(TAG, " Player hit teleport");
                            mapManager.setMap(new Map(gameObjectComponent.map, world, ecsEngine), gameObjectComponent.playerLoc);
                            mapManager.getCurrentMap().isCleared(true);
                        }
                }
            } else if (enemyComponent != null){
                Gdx.app.log(TAG, "Player hit enemy");
                BattleComponent battleComponent = Mapper.battleMapper.get(entity);
                HealthComponent enemyHealth = Mapper.healthMapper.get(collidedEntity);
                HealthComponent playerHealth = Mapper.healthMapper.get(entity);
                Box2DComponent playerBody = Mapper.box2DMapper.get(entity);
                Box2DComponent enemyBody = Mapper.box2DMapper.get(collidedEntity);
                if (battleComponent.attack) {
                    enemyHealth.health --;
                    bounceOff(playerBody.body, enemyBody.body);
                    collision.reset();
                    Gdx.app.log(TAG, "PLAYER HIT ENEMY");
                } else if (enemyComponent.coolDown == 0) {
                    playerHealth.health --;
                    playerHealth.healthBar.setHeartCount(playerHealth.health);
                    enemyComponent.coolDown = 100;
                }

                if (enemyHealth.health == 0) {
                    Gdx.app.log(TAG, "ENEMY IS DEAD!!!!!");
                    ecsEngine.removeEntity(collidedEntity);
                    mapManager.getCurrentMap().setEnemyCount(mapManager.getCurrentMap().getEnemyCount() - 1);
                }
            }
        }
        // Reset the collision component of the player entity
        //collision.reset();
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public void bounceOff(Body attacker, Body defender) {
        float xForce = 0;
        float yForce = 0;

        if (attacker.getPosition().x > defender.getPosition().x) {
            xForce = -20.0f;
        } else if (attacker.getPosition().x < defender.getPosition().x) {
            xForce = 20.0f;
        }

        if (attacker.getPosition().y > defender.getPosition().y) {
            yForce = -20.0f;
        } else if (attacker.getPosition().y < defender.getPosition().y) {
            yForce = 20.0f;
        }

        defender.applyLinearImpulse(xForce, yForce, defender.getPosition().x, defender.getPosition().y, false);
    }
}
