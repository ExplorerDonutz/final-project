package com.quickwoo.finalproject;

import com.badlogic.gdx.math.Vector2;

public class Enemy {

    static Vector2 enemyPos = new Vector2();
    Vector2 direction = new Vector2();
    public float eSpd = 1.5f;

    public void enemyFollow() {
        direction.x = (Player.playerPos.x + 40) - (enemyPos.x + 40);
        direction.y = (Player.playerPos.y + 40) - (enemyPos.y + 40);

        direction.nor();

        enemyPos.x += direction.x * eSpd;
        enemyPos.y += direction.y * eSpd;
    }
}
