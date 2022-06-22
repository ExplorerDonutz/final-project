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
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.map.Map;
import com.quickwoo.finalproject.map.MapManager;
import com.quickwoo.finalproject.map.Maps;
import com.quickwoo.finalproject.screens.GameOverScreen;
import com.quickwoo.finalproject.screens.ScreenType;

public class CollisionSystem extends IteratingSystem {
    private final String TAG = this.getClass().getSimpleName();
    private MapManager mapManager;
    private final World world;
    private final ECSEngine ecsEngine;
    private final FinalProject game;

    public CollisionSystem(World world, ECSEngine ecsEngine, FinalProject game) {
        // Only iterate over entities with a player component
        super(Family.one(PlayerComponent.class).get());
        this.world = world;
        this.ecsEngine = ecsEngine;
        this.game = game;
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
                            mapManager.setMap(Maps.valueOf(gameObjectComponent.map), gameObjectComponent.playerLoc);
                            mapManager.getCurrentMap().isCleared(true);
                        }
                        break;
                    case GameObjectComponent.TYPE_SIGN:
                        Gdx.app.log(TAG, " Player is in interacting distance of sign");
                        gameObjectComponent.isInteractable = true;
                }
            } else if (enemyComponent != null) {
                Gdx.app.log(TAG, "Player hit enemy");
                BattleComponent battleComponent = Mapper.battleMapper.get(entity);
                HealthComponent enemyHealth = Mapper.healthMapper.get(collidedEntity);
                HealthComponent playerHealth = Mapper.healthMapper.get(entity);
                Box2DComponent playerBody = Mapper.box2DMapper.get(entity);
                Box2DComponent enemyBody = Mapper.box2DMapper.get(collidedEntity);
                if (battleComponent.attack) {
                    enemyHealth.health--;
                    bounceOff(playerBody.body, enemyBody.body);
                    collision.reset();
                } else if (enemyComponent.coolDown == 0) {
                    // Lower player health by 1 and set new value to the counter
                    playerHealth.health--;
                    playerHealth.healthBar.setHeartCount(playerHealth.health);
                    // Give player a chance to knock enemies off before they attack again
                    enemyComponent.coolDown = 75;
                }

                if (enemyHealth.health == 0) {
                    if (enemyComponent.isBoss) {
                        // Boss defeated, go to game over screen
                        GameOverScreen gameOverScreen = new GameOverScreen(game);
                        gameOverScreen.isBossDefeated(true);
                        game.getScreenCache().put(ScreenType.GAMEOVER, gameOverScreen);
                        game.setScreen(gameOverScreen);
                    }

                    ecsEngine.removeEntity(collidedEntity);
                    mapManager.getCurrentMap().setEnemyCount(mapManager.getCurrentMap().getEnemyCount() - 1);
                }
            }
        }
        // Reset the collision component of the player entity
        collision.reset();
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
