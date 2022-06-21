package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.ECSEngine;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.input.GameKeyInputListener;
import com.quickwoo.finalproject.input.GameKeys;
import com.quickwoo.finalproject.input.InputManager;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.screens.GameScreen;


public class TextBoxSystem extends IteratingSystem implements GameKeyInputListener {

    private final String TAG = this.getClass().getSimpleName();
    private final ComponentMapper<StateComponent> stateMapper;
    private final ComponentMapper<CollisionComponent> collisionMapper;
    private final ComponentMapper<GameObjectComponent> gameObjectMapper;
    private final ComponentMapper<TextBoxComponent> textBoxMapper;
    private final FinalProject game;
    private final ECSEngine ecsEngine;
    boolean interacted = false;


    public TextBoxSystem(InputManager inputManager, FinalProject game, ECSEngine ecsEngine) {
        super(Family.all(GameObjectComponent.class).get());
        this.game = game;
        this.ecsEngine = ecsEngine;

        stateMapper = Mapper.stateMapper;
        gameObjectMapper = Mapper.gameObjectMapper;
        textBoxMapper = Mapper.textBoxMapper;
        collisionMapper = Mapper.collisionMapper;
        inputManager.addInputListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final GameObjectComponent gameObject = gameObjectMapper.get(entity);
        final CollisionComponent collision = collisionMapper.get(entity);


        if (interacted) {
            if (gameObject.isInteracted) {
                Gdx.app.log(TAG, " Player interacted with sign");
                GameScreen.sign1 = "Hola soy dora";
                GameScreen.isInteracted = true;
                gameObject.isInteracted = false;
            }
        } else {
            GameScreen.isInteracted = false;
        }
    }


    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == GameKeys.INTERACT) {
            if (interacted) {
                interacted = false;
            } else {
                interacted = true;
            }
        }
    }


    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}
