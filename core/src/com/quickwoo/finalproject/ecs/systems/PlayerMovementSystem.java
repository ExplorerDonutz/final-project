package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;

public class PlayerMovementSystem extends IteratingSystem {

    private final ComponentMapper<Box2DComponent> box2DMapper;

    public PlayerMovementSystem() {
        // Iterates through entities that have the player component
        super(Family.all(PlayerComponent.class).get());
        box2DMapper = Mapper.box2DMapper;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2DComponent b2dBody = box2DMapper.get(entity);

        if(Gdx.input.isKeyPressed(Input.Keys.W))
            b2dBody.body.setLinearVelocity(0, 1);
    }
}
