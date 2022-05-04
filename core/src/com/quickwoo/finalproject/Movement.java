package com.quickwoo.finalproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Movement {

    public static float playerx;
    public static float playery;

    private float pSpeed = 3;

    public void movement() {

        //Vertical Movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playery += pSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playery -= pSpeed;
        }

        //Horizontal Movement
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerx -= pSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerx += pSpeed;
        }
    }

    public void edge() {
        if (playerx > Gdx.graphics.getBackBufferWidth() + 20) {
            playerx = -20;
        } else if (playerx < -25) {
            playerx = Gdx.graphics.getBackBufferWidth() + 10;
        }
    }
}
