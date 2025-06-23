package com.javakaian.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.resources.MusicHandler;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.states.State.StateEnum;
import com.javakaian.game.states.StateController;
import com.javakaian.game.util.GameConstants;

public class OTDGame extends ApplicationAdapter {

    private StateController stateController;
    private ShapeRenderer sr;
    private SpriteBatch sb;
    private boolean isFullscreen = false;
    // mengelola siklus hidup aplikasi

    @Override
    public void create() { //inisialisasi file utama
        MusicHandler.init();
        MyAtlas.init();
        stateController = new StateController();
        stateController.setState(StateEnum.MenuState);
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        MusicHandler.playMenuMusic();
    }
    @Override
    public void resize(int width, int height) {
        stateController.resize(width, height);
    }

    @Override //delegasi ke StateController supaya bersih
    public void render() {
        handleFullscreenToggle(); // Togle tombol F11 buat fullsreen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateController.render(sb,sr); //Kasih tau StateController untuk render semua state yang aktif
        stateController.update(Gdx.graphics.getDeltaTime()); //Update logika pergerakan karakter, perhitungan waktu, dll
    }
    private void handleFullscreenToggle() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            isFullscreen = !isFullscreen;
            if (isFullscreen) {
                // Beralih ke mode fullscreen menggunakan display mode saat ini
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                // Kembali ke mode windowed dengan ukuran virtual
                Gdx.graphics.setWindowedMode(
                        (int) GameConstants.VIRTUAL_WIDTH,
                        (int) GameConstants.VIRTUAL_HEIGHT);
            }
        }
    }

    @Override
    public void dispose() {
        MyAtlas.dispose();
    }

}
