package com.quickwoo.finalproject.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.quickwoo.finalproject.ecs.components.*;

public class Mapper {
    public static final ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<Box2DComponent> box2DMapper = ComponentMapper.getFor(Box2DComponent.class);
    public static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<EnemyComponent> enemyMapper = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<GameObjectComponent> gameObjectMapper = ComponentMapper.getFor(GameObjectComponent.class);
    public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<BattleComponent> battleMapper = ComponentMapper.getFor(BattleComponent.class);
    public static final ComponentMapper<TextBoxComponent> textBoxMapper = ComponentMapper.getFor(TextBoxComponent.class);
}
