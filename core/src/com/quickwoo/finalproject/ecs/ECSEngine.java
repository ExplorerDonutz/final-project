package com.quickwoo.finalproject.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.box2d.BodyFactory;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.ecs.components.TransformComponent;
import com.quickwoo.finalproject.ecs.systems.DebugPhysicsSystem;
import com.quickwoo.finalproject.ecs.systems.PhysicsSystem;
import com.quickwoo.finalproject.ecs.systems.PlayerMovementSystem;
import com.quickwoo.finalproject.screens.GameScreen;

public class ECSEngine extends PooledEngine {
    private final BodyFactory bodyFactory;

    public ECSEngine(World world, FinalProject game, GameScreen screen) {
        bodyFactory = BodyFactory.getInstance(world);

        if (FinalProject.DEBUG)
            this.addSystem(new DebugPhysicsSystem(world, game.getCamera()));

        // Add physics system
        this.addSystem(new PhysicsSystem(world));

        // Add player movement system
        this.addSystem(new PlayerMovementSystem(game.getInputManager()));
    }

    public void createPlayer(int x, int y, int drawOrder) {
        final Entity player = this.createEntity();

        // Player
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed = 5.0f;
        player.add(playerComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x,y, 16, 16, BodyDef.BodyType.DynamicBody, true);
        player.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x,y,0);
        transformComponent.scale.set(1,1);
        player.add(transformComponent);

        this.addEntity(player);
    }

    public void createTest(int x, int y, int drawOrder) {
        final Entity test = this.createEntity();

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x,y, 16, 16, BodyDef.BodyType.DynamicBody, true);
        test.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x,y,1);
        transformComponent.scale.set(1,1);
        test.add(transformComponent);

        this.addEntity(test);
    }
}
