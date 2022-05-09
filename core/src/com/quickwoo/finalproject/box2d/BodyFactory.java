package com.quickwoo.finalproject.box2d;

import com.badlogic.gdx.physics.box2d.*;

import static com.quickwoo.finalproject.Constants.PPM;

public class BodyFactory {
    private static BodyFactory thisInstance;
    private World world;

    private BodyFactory(World world) {
        this.world = world;
    }

    public static BodyFactory getInstance(World world) {
        if (thisInstance == null) {
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    public Body makeBox(float posx, float posy, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation) {
        // Create a body definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx / 2 / PPM;
        boxBodyDef.position.y = posy / 2 / PPM;
        boxBodyDef.fixedRotation = fixedRotation;

        // Create the body to attach to the definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.shape = poly;
        boxBody.createFixture(fixtureDef);
        // Dispose of shape now that it has been used
        poly.dispose();

        return boxBody;
    }
}
