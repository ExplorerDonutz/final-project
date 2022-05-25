package com.quickwoo.finalproject.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLoader {
    public final AssetManager manager = new AssetManager();

    // Textures
    public static final String PLAYER_TEXTURE = "PlayerTest.png";
    public static final String ENEMY_TEXTURE = "EnemyTest.png";

    // Map
    public static final String MAP_1 = "map/GrassPLains1.tmx";

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

        // Load maps
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load(MAP_1, TiledMap.class, params);
    }
}