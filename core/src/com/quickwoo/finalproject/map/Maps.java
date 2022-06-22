package com.quickwoo.finalproject.map;

public enum Maps {
    MAP_START ("map/tileStart.tmx"),
    MAP_1 ("map/tilePlains1.tmx"),
    MAP_2 ("map/tilePlains2.tmx"),
    MAP_2_UP ("map/tilePlains2Up.tmx"),
    MAP_2_DOWN ("map/tilePlains2down.tmx"),
    MAP_3 ("map/tilePlains3.tmx"),
    MAP_BOSS ("map/tilePlainsBoss.tmx");

    private final String filepath;

    Maps(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }
}
