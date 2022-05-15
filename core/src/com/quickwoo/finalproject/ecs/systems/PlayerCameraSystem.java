package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.PlayerComponent;
import com.quickwoo.finalproject.ecs.components.TransformComponent;

public class PlayerCameraSystem extends IteratingSystem {
    private final OrthographicCamera camera;

    public PlayerCameraSystem(FinalProject game) {
        super(Family.all(PlayerComponent.class).get());
        camera = game.getCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TransformComponent transformComponent = Mapper.transformMapper.get(entity);
        camera.position.x = MathUtils.lerp(camera.position.x, transformComponent.position.x, 0.1f);
        camera.position.y = MathUtils.lerp(camera.position.y, transformComponent.position.y, 0.1f);
        camera.position.z = 0;
        camera.update();
    }
}
