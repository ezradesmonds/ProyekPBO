package com.javakaian.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.javakaian.game.states.State.StateEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// Kelas ini adalah manajer pusat untuk semua layar (State) dalam game.
public class StateController extends InputAdapter {

    // Menyimpan state yang sudah dibuat untuk digunakan kembali (cache).
    private final Map<StateEnum, State> stateMap;
    // State yang sedang aktif atau ditampilkan.
    private State currentState;
    // State sebelumnya, untuk fungsi "kembali".
    private State previousState;

    // Constructor: inisialisasi map dan menjadikan kelas ini sebagai pendengar input utama.
    public StateController() {
        stateMap = new HashMap<>();
        Gdx.input.setInputProcessor(this);
    }

    // Metode untuk mengganti layar/state yang aktif.
    public void setState(StateEnum stateEnum) {
        previousState = currentState;
        currentState = getState(stateEnum);
        // Pastikan ukuran state baru sesuai dengan jendela saat ini.
        if (currentState != null) {
            currentState.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    // Fungsi untuk kembali ke layar sebelumnya.
    public void goBack() {
        State tmp = previousState;
        previousState = currentState;
        currentState = tmp;
        if (currentState != null) {
            currentState.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    // Mengambil state dari cache. Jika belum ada, maka akan dibuat.
    public State getState(StateEnum stateEnum){
        return stateMap.computeIfAbsent(stateEnum, this::createState);
    }

    // Meneruskan perintah render ke state yang aktif.
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        if(currentState != null) currentState.render(sb,sr);
    }

    // Meneruskan perintah update logika ke state yang aktif.
    public void update(float deltaTime) {
        if(currentState != null) {
            currentState.update(deltaTime);
            Vector2 result = unproject(Gdx.input.getX(),Gdx.input.getY());
            currentState.updateInputs(result.x,result.y);
        }
    }

    // Menerjemahkan koordinat klik layar ke koordinat dunia game.
    private Vector2 unproject(int x, int y){
        if(currentState == null) return null;
        final OrthographicCamera c = currentState.camera;
        Vector3 r = c.unproject(new Vector3(x,y,1));
        return new Vector2(r.x,r.y);
    }

    // Meneruskan event perubahan ukuran jendela ke state aktif.
    public void resize(int width, int height) {
        if (currentState != null) {
            currentState.resize(width, height);
        }
    }

    // Dipanggil saat pemain menekan tombol mouse/layar.
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(currentState != null){
            final Vector2 result = unproject(screenX,screenY);
            currentState.touchDown(result.x,result.y,pointer,button);
        }
        return false;
    }

    // Dipanggil saat pemain melepas tombol mouse/layar.
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(currentState != null){
            final Vector2 result = unproject(screenX,screenY);
            currentState.touchUp(result.x,result.y,pointer,button);
        }
        return false;
    }

    // Dipanggil saat pemain melakukan scroll.
    @Override
    public boolean scrolled(int amount) {
        if(currentState != null) currentState.scrolled(amount);
        return false;
    }

    // Metode "pabrik" untuk membuat instance state baru jika belum ada di cache.
    private State createState(StateEnum stateEnum) {
        switch (stateEnum) {
            case PlayState:
                return new PlayState(this);
            case GameOverState:
                return new GameOverState(this);
            case MenuState:
                return new MenuState(this);
            case OptionState:
                return new OptionsState(this);
            case PauseState:
                return new PauseState(this);
            // case MapSelectState:
            //    return new MapSelectState(this);
            default:
                throw new IllegalArgumentException("Invalid state enum: " + stateEnum);
        }
    }
}