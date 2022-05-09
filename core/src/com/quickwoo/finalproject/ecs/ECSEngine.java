package com.quickwoo.finalproject.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.box2d.BodyFactory;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.ecs.systems.DebugPhysicsSystem;
import com.quickwoo.finalproject.screens.GameScreen;

public class ECSEngine extends PooledEngine {
    private final BodyFactory bodyFactory;

    public ECSEngine(World world, FinalProject game, GameScreen screen) {
        bodyFactory = BodyFactory.getInstance(world);

        if (FinalProject.DEBUG)
            this.addSystem(new DebugPhysicsSystem(world, game.getCamera()));
    }

    public void createPlayer(int x, int y, int drawOrder) {
        final Entity player = this.createEntity();

        // Player
        player.add(this.createComponent(PlayerComponent.class));

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x,y, 32, 32, BodyDef.BodyType.DynamicBody, true);
        player.add(box2DComponent);

        this.addEntity(player);
    }

    public void createTest(int x, int y, int drawOrder) {
        final Entity test = this.createEntity();

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x,y, 32, 32, BodyDef.BodyType.DynamicBody, true);
        test.add(box2DComponent);

        this.addEntity(test);
    }
}
