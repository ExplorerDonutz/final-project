package com.quickwoo.finalproject.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.quickwoo.finalproject.FinalProject;

public class AudioManager {
    private final FinalProject game;
    private final AssetManager assetManager;
    public boolean isPaused;
    private AudioType currentMusicType;
    private Music currentMusic;

    public AudioManager(FinalProject game) {
        this.game = game;
        assetManager = game.getAssetManager().manager;
    }

    public void playAudio(AudioType type) {
        if (type.isMusic()) {
            //Play music
            if (currentMusicType == type) {
                //Audio is already playing
                return;
            } else if (currentMusic != null) {
                currentMusic.stop();
            }

            currentMusicType = type;
            currentMusic = assetManager.get(type.getFilePath(), Music.class);
            currentMusic.setLooping(true);
            currentMusic.play();
        } else {
            //Play sound
            assetManager.get(type.getFilePath(), Music.class).play();
        }

        isPaused = false;
    }

    public void pauseMusic() {
        currentMusic.pause();
        isPaused = true;
    }

    public void resumeMusic() {
        currentMusic.play();
        isPaused = false;
    }
}
