package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.EnemyComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;

public class EnemyMovementSystem extends IteratingSystem {
    private final Vector2 direction = new Vector2();
    private final ComponentMapper<Box2DComponent> box2DMapper;
    private final ComponentMapper<EnemyComponent> enemyMapper;

    public EnemyMovementSystem() {
        super(Family.all(EnemyComponent.class).get());
        box2DMapper = Mapper.box2DMapper;
        enemyMapper = Mapper.enemyMapper;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyComponent = enemyMapper.get(entity);
        Body enemyBody = box2DMapper.get(entity).body;
        Body playerBody = box2DMapper.get(enemyComponent.player).body;

        direction.x = (playerBody.getPosition().x + 40) - (enemyBody.getPosition().x + 40);
        direction.y = (playerBody.getPosition().y + 40) - (enemyBody.getPosition().y + 40);

        direction.nor();

        enemyBody.setLinearVelocity(direction.scl(enemyComponent.speed));
    }
}
