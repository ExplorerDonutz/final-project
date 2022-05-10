package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;

public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {

    private final ComponentMapper<Box2DComponent> box2DMapper;
    private final Vector2 force;

    public PlayerMovementSystem(InputManager inputManager) {
        // Iterates through entities that have the player component
        super(Family.all(PlayerComponent.class).get());
        box2DMapper = Mapper.box2DMapper;
        inputManager.addInputListener(this);
        force = new Vector2(0, 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2DComponent b2dBody = box2DMapper.get(entity);

        b2dBody.body.setLinearVelocity(force.x * 5, force.y * 5);
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        switch (key) {
            case UP:
                force.y = 1;
                break;
            case DOWN:
                force.y = -1;
                break;
            case LEFT:
                force.x = -1;
                break;
            case RIGHT:
                force.x = 1;
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
        switch (key) {
            case LEFT:
                force.x = manager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                force.x = manager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
                break;
            case UP:
                force.y = manager.isKeyPressed(GameKeys.DOWN) ? -1 : 0;
                break;
            case DOWN:
                force.y = manager.isKeyPressed(GameKeys.UP) ? 1 : 0;
        }
    }
}
