/* Michael Quick & Nicholas Woo
 * 14 June 2022
 * System for controlling the players movement
 */
package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.ecs.components.StateComponent;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;

public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {

    private final ComponentMapper<Box2DComponent> box2DMapper;
    private final ComponentMapper<PlayerComponent> playerMapper;
    private final ComponentMapper<StateComponent> stateMapper;
    private final Vector2 force;

    public PlayerMovementSystem(InputManager inputManager) {
        // Iterates through entities that have the player component
        super(Family.all(PlayerComponent.class).get());
        box2DMapper = Mapper.box2DMapper;
        playerMapper = Mapper.playerMapper;
        stateMapper = Mapper.stateMapper;
        inputManager.addInputListener(this);
        force = new Vector2(0, 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Box2DComponent b2dBody = box2DMapper.get(entity);
        final PlayerComponent player = playerMapper.get(entity);
        final StateComponent stateComponent = stateMapper.get(entity);

        b2dBody.body.setLinearVelocity(force.x * player.speed, force.y * player.speed);
        b2dBody.attackBody.setTransform(b2dBody.body.getPosition().x, b2dBody.body.getPosition().y, b2dBody.body.getAngle());

        if (force.isZero()) {
            // Check if the player is attacking
            if (stateComponent.getState() <= StateComponent.STATE_RIGHT) {
                stateComponent.time = 0;
            }

        }

        // Use abs to get non negative values to compare x and y
        if (Math.abs(force.x) > Math.abs(force.y)) {
            if (force.x < 0) {
                stateComponent.isLooping = true;
                stateComponent.setState(StateComponent.STATE_LEFT);
            }

            if (force.x > 0) {
                stateComponent.isLooping = true;
                stateComponent.setState(StateComponent.STATE_RIGHT);
            }
        } else {
            if (force.y < 0) {
                stateComponent.isLooping = true;
                stateComponent.setState(StateComponent.STATE_DOWN);
            }

            if (force.y > 0) {
                stateComponent.isLooping = true;
                stateComponent.setState(StateComponent.STATE_UP);
            }
        }
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        // Check which key direction is pressed, then change the force
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
        // Check which key is pressed, then either reset the force or if the opposite direction is being pressed, make the force opposite
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
