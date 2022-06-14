package com.quickwoo.finalproject.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLoader {
    public final AssetManager manager = new AssetManager();

    // Textures
    public static final String PLAYER_TEXTURE = "PlayerTest.png";
    public static final String PLAYER_ATTACK_UP = "RPG Sprites/sprAttackUp.png";
    public static final String PLAYER_ATTACK_DOWN = "RPG Sprites/sprAttackDown.png";
    public static final String PLAYER_ATTACK_LEFT = "RPG Sprites/sprAttackLeft.png";
    public static final String PLAYER_ATTACK_RIGHT = "RPG Sprites/sprAttackRight.png";
    public static final String ENEMY_TEXTURE = "EnemyTest.png";
    public static final String PLAYER_ATLAS = "sprites/player.atlas";

    // Audio
    // Background music used from https://www.FesliyanStudios.com
    public static final String BACKGROUND_MUSIC = "audio/background.ogg";

    // Map
    public static final String MAP_START = "map/tileStart.tmx";
    public static final String MAP_1 = "map/tilePlains1.tmx";
    public static final String MAP_2 = "map/tilePlains2.tmx";
    public static final String MAP_UP = "map/tilePlains2down.tmx";
    public static final String MAP_DOWN = "map/tilePlains2Up.tmx";

    // Skin
    public static final String SKIN = "skin.json";

    public void queueLoading() {
        // Have to load skin for use in loading screen
        SkinLoader.SkinParameter skinParams = new SkinLoader.SkinParameter("skin.atlas");
        manager.load(SKIN, Skin.class, skinParams);
    }

    public void queueAssets() {
        // Load textures
        manager.load(PLAYER_TEXTURE, Texture.class);
        manager.load(ENEMY_TEXTURE, Texture.class);
        manager.load(PLAYER_ATLAS, TextureAtlas.class);
        manager.load(PLAYER_ATTACK_UP, Texture.class);
        manager.load(PLAYER_ATTACK_DOWN, Texture.class);
        manager.load(PLAYER_ATTACK_LEFT, Texture.class);
        manager.load(PLAYER_ATTACK_RIGHT, Texture.class);

        // Load audio
        manager.load(BACKGROUND_MUSIC, Music.class);

        // Load maps
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load(MAP_START, TiledMap.class, params);
        manager.load(MAP_1, TiledMap.class, params);
        manager.load(MAP_2, TiledMap.class, params);
        manager.load(MAP_UP, TiledMap.class, params);
        manager.load(MAP_DOWN, TiledMap.class, params);
    }
}
