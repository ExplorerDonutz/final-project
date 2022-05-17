package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;

import static com.quickwoo.finalproject.Constants.*;

public class TileMaps{
    public static OrthographicCamera cam;
    private final TiledMap tiledMap;
    private static OrthogonalTiledMapRenderer mapRenderer = null;

    //Set up the tiledmap

    public TileMaps(FinalProject game) {
        tiledMap = new TmxMapLoader().load("Map/GrassPlains1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.PIXELS_TO_METERS, game.getBatch());
        int objectLayerID = 2;
        //Collision with collision layer of tilemap
        TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)tiledMap.getLayers().get(objectLayerID);
        MapObjects object = collisionObjectLayer.getObjects();


    }


    //Set up the rendering of the map
    public static void renderCam(OrthographicCamera cam) {
        mapRenderer.setView(cam);
        mapRenderer.render();
    }

    // Create camera and set position with scale
    public static void createCam() {
        cam = new OrthographicCamera(WIDTH / PPM, HEIGHT / PPM);
        cam.position.set(WIDTH / PPM / SCALE, HEIGHT / PPM / SCALE, 0);

        cam.update();
    }


}
