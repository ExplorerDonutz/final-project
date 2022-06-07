package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.AnimationComponent;
import com.quickwoo.finalproject.ecs.components.StateComponent;
import com.quickwoo.finalproject.ecs.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
    private final ComponentMapper<AnimationComponent> animationMapper;
    private final ComponentMapper<StateComponent> stateMapper;
    private final ComponentMapper<TextureComponent> textureMapper;

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
        animationMapper = Mapper.animationMapper;
        stateMapper = Mapper.stateMapper;
        textureMapper = Mapper.textureMapper;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = animationMapper.get(entity);
        final StateComponent stateComponent = stateMapper.get(entity);
        TextureComponent textureComponent = textureMapper.get(entity);
        textureComponent.region = animationComponent.animations.get(stateComponent.getState()).getKeyFrame(stateComponent.time, stateComponent.isLooping);
        stateComponent.time += deltaTime;
    }
}
