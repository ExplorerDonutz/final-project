package com.quickwoo.finalproject.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameObject {
    private final int type;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float rotation;
    private final TextureRegion region;
    private final String nextMap;
    private final int playerLoc;
    private final String text;

    public GameObject(int type, float x, float y, float width, float height, float rotation, TextureRegion region) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.region = region;
        nextMap = null;
        playerLoc = -1;
        text = null;
    }

    public GameObject(int type, float x, float y, float width, float height, float rotation, String text, TextureRegion region) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.region = region;
        this.text = text;
        nextMap = null;
        playerLoc = -1;
    }

    public GameObject(int type, float x, float y, float width, float height, float rotation, String nextMap, int playerLoc, TextureRegion region) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.region = region;
        this.nextMap = nextMap;
        this.playerLoc = playerLoc;
        text = null;
    }

    public int getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getPlayerLoc() {
        return playerLoc;
    }

    public float getRotation() {
        return rotation;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public String getNextMap() {
        return nextMap;
    }

    public String getText() {
        return text;
    }
}
