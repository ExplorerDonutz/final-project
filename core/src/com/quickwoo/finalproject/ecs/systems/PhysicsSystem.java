package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.TransformComponent;

public class PhysicsSystem extends IntervalIteratingSystem {
    private final World world;
    private final Array<Entity> bodyQueue;

    public PhysicsSystem(World world) {
        super(Family.all(Box2DComponent.class).get(), Constants.TIME_STEP);
        this.world = world;
        bodyQueue = new Array<>();
    }

    @Override
    protected void processEntity(Entity entity) {
        bodyQueue.add(entity);
    }

    @Override
    protected void updateInterval() {
        world.step(Constants.TIME_STEP, 6, 2);
        super.updateInterval();

        //Entity Queue
        for (Entity entity : new Array.ArrayIterator<>(bodyQueue)) {
            TransformComponent transComponent = Mapper.transformMapper.get(entity);
            Box2DComponent bodyComponent = Mapper.box2DMapper.get(entity);
            Vector2 position = bodyComponent.body.getPosition();

            transComponent.position.x = position.x;
            transComponent.position.y = position.y;
            transComponent.rotation = bodyComponent.body.getAngle() * MathUtils.radiansToDegrees;
        }
        bodyQueue.clear();
    }
}
