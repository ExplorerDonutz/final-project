package com.quickwoo.finalproject;

public class Enemy {

    public static int enemyx;
    public static int enemyy;
    public float eSpeed = 1.5f;

    public void enemy() {
        if (enemyx > Movement.playerx) {
            enemyx -= eSpeed;
        } else {
            enemyx += eSpeed;
        }

        if (enemyy > Movement.playery) {
            enemyy -= eSpeed;
        } else {
            enemyy += eSpeed;
        }
    }
}
