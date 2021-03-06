/* Michael Quick & Nicholas Woo
 *  26 May 2022
 *  System to make the camera follow the player
 */
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
import com.quickwoo.finalproject.map.Map;

public class PlayerCameraSystem extends IteratingSystem {
    private final OrthographicCamera camera;
    private Map map;

    public PlayerCameraSystem(FinalProject game) {
        // Set the system to only iterate over entities with the player component
        super(Family.all(PlayerComponent.class).get());
        camera = game.getCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Get the entity's transform component
        final TransformComponent transformComponent = Mapper.transformMapper.get(entity);

        // Linearly interpolate the camera's new location as it follows the entity's position
        camera.position.x = MathUtils.lerp(camera.position.x, transformComponent.position.x, 0.1f);
        camera.position.y = MathUtils.lerp(camera.position.y, transformComponent.position.y, 0.1f);

        // Clamp camera inside map boundaries
        if (map != null) {
            camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth / 2f, map.getMap().getProperties().get("width", Integer.class) - camera.viewportWidth / 2f);
            camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2f, map.getMap().getProperties().get("height", Integer.class) - camera.viewportHeight / 2f);
        }

        camera.position.z = 0;
        camera.update();
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
