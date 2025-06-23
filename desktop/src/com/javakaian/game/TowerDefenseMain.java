package com.javakaian.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.javakaian.game.util.GameConstants;

public class TowerDefenseMain {

    public static void main(String[] args) {
        //titik masuk aplikasi untuk platform Desktop
        OTDGame game = new OTDGame();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration(); //konfigurasi jendela awal
        config.width = (int) GameConstants.VIRTUAL_WIDTH;
        config.height = (int) GameConstants.VIRTUAL_HEIGHT;
        config.resizable = true;
        new LwjglApplication(game, config);
    }
}
