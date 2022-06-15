/* Michael Quick & Nicholas Woo
 * 14 June 2022
 * System for showing debug information for box2d physics
 */
package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;

public class DebugPhysicsSystem extends IteratingSystem {
    private final Box2DDebugRenderer debugRenderer;
    private final World world;
    private final OrthographicCamera cam;

    public DebugPhysicsSystem(World world, OrthographicCamera cam) {
        // Set system to iterate over all entities with a box2D component
        super(Family.all(Box2DComponent.class).get());
        debugRenderer = new Box2DDebugRenderer();
        this.world = world;
        this.cam = cam;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Debug render the physics world
        debugRenderer.render(world, cam.combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Nothing to do here
    }
}
