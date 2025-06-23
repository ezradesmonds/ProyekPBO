package com.javakaian.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.entity.Bounty;
import com.javakaian.game.entity.Enemy;
import com.javakaian.game.level.Level;
import com.javakaian.game.map.Map;
import com.javakaian.game.map.MapMaker.Direction;
import com.javakaian.game.util.GameConstants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//Spawn Musuh, Ngatur Musuh, Nambah Wave, Nambah Bounty, Nambah Health, Nambah Enemy Limit
public class EnemyController {

    //Musuh yang ada di layar
    private final List<Enemy> enemyList;
    //Waktu untuk spawn musuh
    private float spawnTime = 0;
    //Periode spawn musuh
    private float spawnPeriod;
//Jumlah musuh yang sudah di spawn
    private int count;

    //Health musuh
    private float health;
    //Batas jumlah musuh yang bisa ada di layar
    private int enemyNumberLimit;
    //Bounty yang didapatkan ketika musuh mati
    private int bounty;
    //Daftar arah yang bisa diambil musuh
    private final LinkedList<Direction> directionList;

    private int nextWaveTimer = 0;

    //Reff ke object level kalo musuh lolos atau mati
    private final Level level;

    private int enemySpeed = GameConstants.ENEMY_SPEED;

    //Animasi Bounty
    private final List<Bounty> bountyList;

    public EnemyController(Level level) {

        this.level = level;
        Map map = level.getMap(); //ambil map dari level
        this.directionList = map.getDirectionList(); //jalur
        this.health = level.getEnemyHealth(); //health musuh
        this.enemyNumberLimit = level.getEnemyNumber(); //batas jumlah musuh yang bisa ada di layar

        count = level.getEnemyNumber();
        enemyList = new ArrayList<>(); //tampung musuh
        bountyList = new ArrayList<>(); //nampung bounty

        this.bounty = GameConstants.ENEMY_BOUNTY;
        this.spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD;
    }

    //render musuh dan bounty
    public void render(SpriteBatch sb) {
        for (Enemy enemy : enemyList) {
            enemy.render(sb);
        }
        for (Bounty bounty : bountyList) {
            bounty.render(sb);
        }
    }

    //render health bar musuh
    public void render(ShapeRenderer sr) {
        for (Enemy enemy : enemyList) {
            enemy.render(sr);
        }
    }

    public void update(float deltaTime) {
        //update gerak, status, animasi bounty
        for (Enemy enemy : enemyList) {
            enemy.update(deltaTime);
        }
        //update gerak, status, animasi bounty
        for (Bounty bounty : bountyList) {
            bounty.update(deltaTime);
        }
        removeCoins();
        createEnemy();
        removeDeadEnemies();
        checkIfEnemyOfScreen();
    }

    public void createEnemy() {
        spawnTime += Gdx.graphics.getDeltaTime();
        if (spawnTime >= spawnPeriod) {
            spawnTime = 0;
            if (count < enemyNumberLimit) {
                Vector2 p = new Vector2(0, 0); //spawn diluar layar kiri
                enemyList.add(new Enemy(
                        p.x - GameConstants.GRID_WIDTH + (GameConstants.GRID_WIDTH / 2 - GameConstants.ENEMY_WIDTH / 2),
                        p.y + GameConstants.GRID_HEIGHT
                                + (GameConstants.GRID_HEIGHT / 2 - GameConstants.ENEMY_HEIGHT / 2),
                        GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT, health, directionList, bounty,
                        enemySpeed));
                count++;
            } else {
                isWaveCompleted();
            }
        }
    }

    private void isWaveCompleted() {
        if (enemyList.isEmpty()) {
            nextWaveTimer++;
            if (nextWaveTimer % 2 == 0)
                level.nextWaveCountDown((GameConstants.NEXT_WAVE_SPAWN_TIME / 2 - nextWaveTimer / 2));

            //Round wave selesai
            if (nextWaveTimer == GameConstants.NEXT_WAVE_SPAWN_TIME) {
                count = 0;
                nextWaveTimer = 0;
                this.health *= 2;
                this.enemyNumberLimit += 1;
                this.bounty += 7;
                level.newWaveCreated(this.enemyNumberLimit);
            }
        }
    }

    private void checkIfEnemyOfScreen() {

        List<Enemy> shouldRemoved = new ArrayList<>();
        for (Enemy e : enemyList) {
            if (e.position.x + e.size.x > GameConstants.VIRTUAL_WIDTH) {
                shouldRemoved.add(e);
                level.enemyPassedTheCheckPoint();
            }
        }
        enemyList.removeAll(shouldRemoved);
    }

    private void removeCoins() {
        List<Bounty> bounties = new ArrayList<>();
        for (Bounty b : bountyList) {
            if (b.isDisposable()) {
                bounties.add(b);
            }
        }
        bountyList.removeAll(bounties);
    }

    public void removeDeadEnemies() {
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy e = enemyList.get(i);
            if (!e.isAlive()) {
                level.enemyKilled(e.getBounty());
                enemyList.remove(i);
                bountyList.add(
                        new Bounty(e.position.x, e.position.y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT));
                i--;
            }
        }
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public void speed2xClicked() {
        enemySpeed = GameConstants.ENEMY_SPEED * 2;
        spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD / 2;
        for (Enemy enemy : enemyList) {
            enemy.speed2XClicked();

        }
    }

    public void normalSpeedClicked() {
        enemySpeed = GameConstants.ENEMY_SPEED;
        spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD;
        for (Enemy enemy : enemyList) {
            enemy.normalSpeedClicked();

        }
    }
}
