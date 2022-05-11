package com.quickwoo.finalproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player{


    static Vector2 playerPos = new Vector2();
    private float pSpd = 3;

    public void movement() {

        //Vertical Movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerPos.y += pSpd;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerPos.y -= pSpd;
        }

        //Horizontal Movement
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerPos.x -= pSpd;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerPos.x += pSpd;
        }
    }


    //Collsion with edge
    public void edge() {
        if (playerPos.x > Gdx.graphics.getBackBufferWidth() + 20) {
            playerPos.x = -20;
        } else if (playerPos.x < -25) {
            playerPos.x = Gdx.graphics.getBackBufferWidth() + 10;
        }
    }






}
