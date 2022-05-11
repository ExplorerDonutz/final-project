package com.quickwoo.finalproject;

import com.badlogic.gdx.physics.box2d.*;

public class Collisions implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Collision");
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
