package com.javakaian.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
//import java.util.Random;

public class MyAtlas {

    public enum Theme {
        GREEN, BROWN
    }

    public static Sprite MENU_TILE;
    public static TextureRegion LAND_TILE;
    public static TextureRegion PATH_TILE;

    public static Sprite ENEMY;
    public static Sprite ENEMY_SLOWED;

    public static Sprite FIRE_TOWER;
    public static Sprite ICE_TOWER;
    public static Sprite ELECTRIC_TOWER;
    public static Sprite FIRE_BULLET;
    public static Sprite ICE_BULLET;

    public static Sprite DAMAGE;
    public static Sprite RANGE;
    public static Sprite SPEED;

    public static Sprite MENU_BUTTON;
    public static Sprite GENERIC_BUTTON;
    public static Sprite SOUND_ON;
    public static Sprite SOUND_OFF;
    public static Sprite MUSIC_ON;
    public static Sprite MUSIC_OFF;
    public static Sprite RESUME_GAME;
    public static Sprite RESTART_GAME;
    public static Sprite MENU_PLAY;

    public static Sprite WAVE_SLOW;
    public static Sprite WAVE_FAST;
    public static Sprite WAVE_PAUSE;
    public static Sprite WAVE_RESUME;
    public static Sprite CHANGE_MAP;
    public static Sprite QUIT_X;

    public static Array<AtlasRegion> coinRegions;
    private static TextureAtlas atlas;
    public static void loadMapTheme(Theme theme) {
        // Hapus tekstur lama jika sudah ada untuk menghindari memory leak
        if (LAND_TILE != null && LAND_TILE.getTexture() != null) LAND_TILE.getTexture().dispose();
        if (PATH_TILE != null && PATH_TILE.getTexture() != null) PATH_TILE.getTexture().dispose();

        switch (theme) {
            case GREEN:
                LAND_TILE = new TextureRegion(new Texture(Gdx.files.internal("tileField.png")));
                PATH_TILE = new TextureRegion(new Texture(Gdx.files.internal("tilePath.png")));
                break;
            case BROWN:
                LAND_TILE = new TextureRegion(new Texture(Gdx.files.internal("FieldsTile_Brown.png")));
                PATH_TILE = new TextureRegion(new Texture(Gdx.files.internal("tilePath.png")));
                break;
        }
    }

    public static void init() {
        atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
        atlas.getTextures().
                forEach(t -> t.setFilter(TextureFilter.Linear, TextureFilter.Linear));
        coinRegions = atlas.findRegions("coin");

        MENU_TILE = createSprite(atlas.findRegion("menu"));
//        LAND_TILE = new TextureRegion(new Texture(Gdx.files.internal("FieldsTile_Brown.png")));
//        PATH_TILE = new TextureRegion(new Texture(Gdx.files.internal("tileField.png")));
        ENEMY = createSprite(atlas.findRegion("enemy"));
        ENEMY_SLOWED = createSprite(atlas.findRegion("enemy_slowed"));
        FIRE_TOWER = new Sprite(new Texture(Gdx.files.internal("fireTower.png")));
        ELECTRIC_TOWER = new Sprite(new Texture(Gdx.files.internal("electricTower.png")));
        ICE_TOWER = new Sprite(new Texture(Gdx.files.internal("iceTower.png")));
        FIRE_BULLET = createSprite(atlas.findRegion("bullet_fire"));
        ICE_BULLET = createSprite(atlas.findRegion("ice_bullet"));

        DAMAGE = createSprite(atlas.findRegion("attack_menu"));
        RANGE = createSprite(atlas.findRegion("range_menu"));
        SPEED = createSprite(atlas.findRegion("speed_menu"));

        WAVE_PAUSE = createSprite(atlas.findRegion("pause_menu_item"));
        WAVE_RESUME = createSprite(atlas.findRegion("resume_menu_item"));
        WAVE_SLOW = createSprite(atlas.findRegion("menu_item_2x"));
        WAVE_FAST = createSprite(atlas.findRegion("menu_item_2x_pressed"));
        CHANGE_MAP = createSprite(atlas.findRegion("btn_remake"));
        QUIT_X = createSprite(atlas.findRegion("btn_quit"));

        MENU_BUTTON = createSprite(atlas.findRegion("menu_button"));
        RESTART_GAME = createSprite(atlas.findRegion("replay_button"));

        MENU_PLAY = createSprite(atlas.findRegion("play"));
        RESUME_GAME = createSprite(atlas.findRegion("resume"));
        SOUND_ON = createSprite(atlas.findRegion("sound_on"));
        SOUND_OFF = createSprite(atlas.findRegion("sound_off"));
        MUSIC_ON = createSprite(atlas.findRegion("music_on"));
        MUSIC_OFF = createSprite(atlas.findRegion("music_off"));
        GENERIC_BUTTON = createSprite(atlas.findRegion("empty_button"));
    }

    public static Sprite createSprite(AtlasRegion region) {
        final Sprite s = new Sprite(region);
        s.flip(false, true);
        return s;
    }

    public static void dispose() {
        atlas.dispose();
    }

}
