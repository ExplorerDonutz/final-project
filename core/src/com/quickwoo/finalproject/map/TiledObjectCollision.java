/* Michael Quick & Nicholas Woo
 * 14 June 2022
 * Utility class for creating the bodies of map collision layers
 */
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

        // Iterate through all the map objects on the collision layer
        for (MapObject object : objects) {
            Shape shape;
            // Create the shape depending on the type of map object the object is
            if (object instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) object, bodyDef);
            } else if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object, bodyDef);
            } else if (object instanceof PolygonMapObject) {
                shape = createPolygon((PolygonMapObject) object, bodyDef);
            } else {
                // The type is not supported, skip the object
                continue;
            }
            Body body;
            // These are boundaries so they should be static
            bodyDef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bodyDef);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1.0f;

            // Set the shape of the body to the shape created
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

            // The shape is no longer needed, so it should be disposed
            shape.dispose();

            // Add the body to the body array
            bodies.add(body);
        }

        // Return all the bodies created from the collision layer
        return bodies;
    }

    // Method for creating polygon shapes from map objects
    private static Shape createPolygon(PolygonMapObject polygon, BodyDef bodyDef) {
        float[] vertices = polygon.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        bodyDef.position.set(0,0);
        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    // Method for creating polyline chains from map objects
    private static ChainShape createPolyLine(PolylineMapObject polyline, BodyDef bodyDef) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        bodyDef.position.set(0,0);
        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    // Method for creating rectangles from map objects
    private static Shape createRectangle(RectangleMapObject rectangle, BodyDef def){
        Rectangle rect = rectangle.getRectangle();

        PolygonShape shape = new PolygonShape();

        def.position.set((rect.x + rect.width / 2) / Constants.PPM, (rect.y + rect.height / 2) / Constants.PPM);

        shape.setAsBox(rect.width / 2 / Constants.PPM, rect.height / 2 / Constants.PPM);

        return shape;
    }
}
