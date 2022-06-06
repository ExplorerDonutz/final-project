package com.quickwoo.finalproject.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {

    private static int FRAME_COLS = 6;
    private static int FRAME_ROWS = 1;
    Texture walkSheet;
    Animation<TextureRegion> walkLeftAnimation;
    float stateTime;
    TextureRegion reg;
    public float speed = 0;

    @Override
    public void reset() {
        speed = 0;
    }

    //Animations
    public void playerLeftAnimation() {
        walkSheet = new Texture(Gdx.files.internal("RPG Sprites/spr_player_left_strip6.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; i < FRAME_COLS; i++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkLeftAnimation = new Animation<TextureRegion>(0.025f, walkFrames);
        stateTime = 0f;
        reg = walkLeftAnimation.getKeyFrame(0);
    }

    public void act(float delta) {

        stateTime += delta;
        reg = walkLeftAnimation.getKeyFrame(stateTime,true);
    }

}
