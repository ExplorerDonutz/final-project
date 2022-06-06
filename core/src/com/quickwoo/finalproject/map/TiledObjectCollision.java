package com.quickwoo.finalproject.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;

public class TiledObjectCollision {
    public static Array<Body> parseTiledObjectLayer(World world, MapObjects objects) {
        Array<Body> bodies = new Array<>();
        BodyDef bodyDef = new BodyDef();
        for (MapObject object : objects) {
            Shape shape;
            if (object instanceof PolygonMapObject) {
                shape = createPolygon((PolygonMapObject) object);
            } else if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object, bodyDef);
            } else {
                continue;
            }
            Body body;
            bodyDef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bodyDef);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1.0f;
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            shape.dispose();
            bodies.add(body);
        }

        return bodies;
    }

    private static ChainShape createPolygon(PolygonMapObject polygon) {
        float[] vertices = polygon.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    private static Shape createRectangle(RectangleMapObject rectangle, BodyDef def) {
        Rectangle rect = rectangle.getRectangle();

        PolygonShape shape = new PolygonShape();

        def.position.set((rect.x + rect.width / 2) / Constants.PPM, (rect.y + rect.height / 2) / Constants.PPM);

        shape.setAsBox(rect.width / 2 / Constants.PPM, rect.height / 2 / Constants.PPM);

        return shape;
    }
}
