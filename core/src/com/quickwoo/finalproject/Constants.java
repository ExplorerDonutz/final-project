/* Michael Quick
*  May 7, 2022
*  Static class for storing constant variables
 */
package com.quickwoo.finalproject;

import com.badlogic.gdx.Gdx;

public class Constants {
    // Camera Scale
    public static final float SCALE = 8.0f;

    // Game width and height
    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();

    // Pixel per meter
    public static final float PPM = 16f;

    // Pixels to meters
    public static final float PIXELS_TO_METERS = 1.0f / PPM;

    // Box2D Timestep
    public static final float TIME_STEP = 1/45f;
}
