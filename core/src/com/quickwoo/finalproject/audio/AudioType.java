package com.quickwoo.finalproject.audio;

import com.quickwoo.finalproject.loader.AssetLoader;

public enum AudioType {
    BACKGROUND(AssetLoader.BACKGROUND_MUSIC, true, 0.5f);

    private final String filePath;
    private final boolean isMusic;

    AudioType(String filePath, boolean isMusic, float volume) {
        this.filePath = filePath;
        this.isMusic = isMusic;
    }

    public String getFilePath() {
        return filePath;
    }

    public Boolean isMusic() {
        return isMusic;
    }
}
