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

    public GameObject(int type, float x, float y, float width, float height, float rotation, TextureRegion region) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.region = region;
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

    public float getRotation() {
        return rotation;
    }

    public TextureRegion getRegion() {
        return region;
    }
}
