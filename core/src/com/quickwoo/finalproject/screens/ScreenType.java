/* Michael Quick & Nicholas Woo
 * 21 June 2022
 * Enums for the different screens
 */
package com.quickwoo.finalproject.screens;

import com.badlogic.gdx.Screen;

public enum ScreenType {
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class),
    MENU(MenuScreen.class),
    GAMEOVER(GameOverScreen.class),
    TUTORIAL(TutorialScreen.class);
    private final Class<? extends Screen> screenClass;

    ScreenType(final Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}

