package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;

import javax.swing.plaf.nimbus.State;

public class PlayerCombatSystem  extends IteratingSystem implements GameKeyInputListener {

    private final ComponentMapper<StateComponent> stateMapper;
    private final ComponentMapper<PlayerComponent> playerMapper;
    private final ComponentMapper<BattleComponent> battleMapper;
    private final ComponentMapper<HealthComponent> healthMapper;
    private final ComponentMapper<AnimationComponent> animationMapper;
    private boolean spacePressed = false;


    public PlayerCombatSystem(InputManager inputManager) {
        super(Family.all(PlayerComponent.class).get());
        stateMapper = Mapper.stateMapper;
        playerMapper = Mapper.playerMapper;
        battleMapper = Mapper.battleMapper;
        healthMapper = Mapper.healthMapper;
        animationMapper = Mapper.animationMapper;
        inputManager.addInputListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final StateComponent stateComponent = stateMapper.get(entity);
        final AnimationComponent animationComponent = animationMapper.get(entity);
        stateComponent.isLooping = false;

        if (spacePressed) {
            if (stateComponent.getState() == StateComponent.STATE_DOWN) {
                stateComponent.setState(StateComponent.STATE_ATTACK_DOWN);
            } else if (stateComponent.getState() == StateComponent.STATE_UP) {
                stateComponent.setState(StateComponent.STATE_ATTACK_UP);
            } else if (stateComponent.getState() == StateComponent.STATE_LEFT) {
                stateComponent.setState(StateComponent.STATE_ATTACK_LEFT);
            } else if (stateComponent.getState() == StateComponent.STATE_RIGHT) {
                stateComponent.setState(StateComponent.STATE_ATTACK_RIGHT);
            }

            if (animationComponent.animations.get(stateComponent.getState()).isAnimationFinished(stateComponent.time)) {
                if (stateComponent.getState() == StateComponent.STATE_ATTACK_DOWN) {
                    stateComponent.setState(StateComponent.STATE_DOWN);
                } else if (stateComponent.getState() == StateComponent.STATE_ATTACK_UP) {
                    stateComponent.setState(StateComponent.STATE_UP);
                } else if (stateComponent.getState() == StateComponent.STATE_ATTACK_LEFT) {
                    stateComponent.setState(StateComponent.STATE_LEFT);
                } else if (stateComponent.getState() == StateComponent.STATE_ATTACK_RIGHT) {
                    stateComponent.setState(StateComponent.STATE_RIGHT);
                }
                spacePressed = false;
            }
        }


    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == GameKeys.SPACE) {
            spacePressed = true;

        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
    }
}
