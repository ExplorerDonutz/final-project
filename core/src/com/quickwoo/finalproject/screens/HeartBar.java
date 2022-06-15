package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class HeartBar extends Widget implements Disableable {

    private boolean isDisabled = false;
    private int heartCount;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Array<Drawable> hearts;

    public HeartBar(int heartCount, int width, int height, int x, int y, Skin skin) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.heartCount = heartCount;
        hearts = new Array<>();

        for (int i = 0; i < heartCount; i++) {
            hearts.add(skin.getDrawable("heart"));
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (!isDisabled) {

            for (int i = 0; i < heartCount; i++) {

                hearts.get(i).draw(batch, x + (i * width), y, width, height);
            }
        }
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        isDisabled = true;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public int getHeartCount() {
        return heartCount;
    }
}
