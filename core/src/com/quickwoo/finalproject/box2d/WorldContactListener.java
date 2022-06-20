package com.quickwoo.finalproject.box2d;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.BattleComponent;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
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
            Box2DComponent box2DComponent;
            entity = (Entity) fa.getBody().getUserData();
            entity2 = (Entity) fb.getBody().getUserData();

            if (fa.getBody().getType().equals(BodyDef.BodyType.StaticBody)) {
                box2DComponent = Mapper.box2DMapper.get(entity2);
                if (fb.getBody().equals(box2DComponent.attackBody)) {
                    entityCollision(entity2, fa, true);
                } else {
                    entityCollision(entity2, fa, false);
                }

            } else {
                box2DComponent = Mapper.box2DMapper.get(entity);
                if (fa.getBody().equals(box2DComponent.attackBody)) {
                    entityCollision(entity, fb, true);
                } else {
                    entityCollision(entity, fb, false);
                }
            }
        } else if (fb.getBody().getUserData() instanceof Entity) {
            entity = (Entity) fb.getBody().getUserData();
            Box2DComponent box2DComponent = Mapper.box2DMapper.get(entity);
            if (fb.getBody().equals(box2DComponent.attackBody)) {
                entityCollision(entity, fa, true);
            } else {
                entityCollision(entity, fa, false);
            }
        }
    }

    private void entityCollision(Entity entity, Fixture fb, boolean isAttackBody) {
        if (fb.getBody().getUserData() instanceof Entity) {
            collisionEntity = (Entity) fb.getBody().getUserData();

            CollisionComponent collisionA = collisionMapper.get(entity);
            CollisionComponent collisionB = collisionMapper.get(collisionEntity);
            BattleComponent battleComponent = Mapper.battleMapper.get(entity);

            if (battleComponent != null) {
                if (battleComponent.attack) {
                    if (collisionA != null) {
                        collisionA.collisionEntity = collisionEntity;
                    } else if (collisionB != null) {
                        collisionB.collisionEntity = entity;
                    }
                } else if (!isAttackBody) {
                    if (collisionA != null) {
                        collisionA.collisionEntity = collisionEntity;
                    } else if (collisionB != null) {
                        collisionB.collisionEntity = entity;
                    }
                }
            } else {
                if (collisionA != null) {
                    collisionA.collisionEntity = collisionEntity;
                } else if (collisionB != null) {
                    collisionB.collisionEntity = entity;
                }
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
