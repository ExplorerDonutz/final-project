package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.GameObjectComponent;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.screens.GameScreen;

public class InteractionSystem extends IteratingSystem implements GameKeyInputListener {

    private final FinalProject game;
    private final ECSEngine ecsEngine;
    private final ComponentMapper<GameObjectComponent> gameObjectMapper;
    private boolean isInteracted = false;
    private final GameScreen gameScreen;


    public InteractionSystem(FinalProject game, ECSEngine ecsEngine, GameScreen gameScreen) {
        super(Family.all(GameObjectComponent.class).get());
        this.game = game;
        game.getInputManager().addInputListener(this);
        this.ecsEngine = ecsEngine;
        this.gameScreen = gameScreen;
        gameObjectMapper = Mapper.gameObjectMapper;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final GameObjectComponent gameObjectComponent = gameObjectMapper.get(entity);

        if (isInteracted) {
            if (gameObjectComponent.isInteractable) {
                gameScreen.setSignText("SUCK");
                gameObjectComponent.isInteractable = false;
                gameScreen.signInteracted(true);
            }
            }else {
            gameScreen.signInteracted(false);
        }
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == GameKeys.INTERACT)
            isInteracted = !isInteracted;
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
    }
}
