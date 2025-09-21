package com.engine;

import com.engine.background.Background;

public class Game {
    private static Game instance;
    private Background currentBackground;
    private Game() {}
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Background getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(Background currentBackground) {
        this.currentBackground = currentBackground;
        System.out.println("background was succesfully changed to: " + currentBackground.getImage());
    }
}