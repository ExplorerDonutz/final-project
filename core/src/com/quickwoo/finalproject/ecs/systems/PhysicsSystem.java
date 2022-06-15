package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {
    private final World world;
    private final Array<Entity> bodyQueue;

    public PhysicsSystem(World world) {
        super(Family.all(Box2DComponent.class).get());
        this.world = world;
        bodyQueue = new Array<>();
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        bodyQueue.add(entity);
}

    @Override
    public void update(float delta) {
        super.update(delta);
        world.step(Constants.TIME_STEP, 8, 3);

        // Iterate through all the bodies in the queue & update them
        for (Entity entity : new Array.ArrayIterator<>(bodyQueue)) {
            TransformComponent transComponent = Mapper.transformMapper.get(entity);
            Box2DComponent bodyComponent = Mapper.box2DMapper.get(entity);
            Vector2 position = bodyComponent.body.getPosition();

            transComponent.position.x = position.x;
            transComponent.position.y = position.y;
            transComponent.rotation = bodyComponent.body.getAngle() * MathUtils.radiansToDegrees;
        }

        // Clear the queue once all the bodies have been updated
        bodyQueue.clear();
    }
}
