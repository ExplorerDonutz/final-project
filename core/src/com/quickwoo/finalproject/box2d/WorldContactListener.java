package com.quickwoo.finalproject.box2d;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.CollisionComponent;

public class WorldContactListener implements ContactListener {
    private final String TAG = this.getClass().getSimpleName();
    private Entity entity;
    private Entity entity2;
    private Entity collisionEntity;
    private final ComponentMapper<CollisionComponent> collisionMapper;

    public WorldContactListener() {
        collisionMapper = Mapper.collisionMapper;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Gdx.app.log(TAG, fa.getBody().getType() + " has hit " + fb.getBody().getType());

        if (fa.getBody().getUserData() instanceof Entity) {
            entity = (Entity) fa.getBody().getUserData();
            entity2 = (Entity) fb.getBody().getUserData();

            if (fa.getBody().getType().equals(BodyDef.BodyType.StaticBody)) {
                entityCollision(entity2, fa);
            } else {
                entityCollision(entity, fb);
            }
        } else if (fb.getBody().getUserData() instanceof Entity) {
            entity = (Entity) fb.getBody().getUserData();
            entityCollision(entity, fa);
        }
    }

    private void entityCollision(Entity entity, Fixture fb) {
        if (fb.getBody().getUserData() instanceof Entity) {
            collisionEntity = (Entity) fb.getBody().getUserData();

            CollisionComponent collisionA = collisionMapper.get(entity);
            CollisionComponent collisionB = collisionMapper.get(collisionEntity);

            if (collisionA != null) {
                collisionA.collisionEntity = collisionEntity;
            } else if (collisionB != null) {
                collisionB.collisionEntity = entity;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        CollisionComponent collisionComponent = Mapper.collisionMapper.get(entity);

        if (collisionComponent != null)
            collisionComponent.reset();

        Gdx.app.log(TAG, "Collision ended");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
