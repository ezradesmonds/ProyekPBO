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

// Kelas ini bertugas untuk mengelola semua musuh (Enemy) di dalam game.
public class EnemyController {

    // Daftar untuk menyimpan semua objek musuh yang aktif.
    private final List<Enemy> enemyList;
    // Timer untuk mengatur interval kemunculan musuh.
    private float spawnTime = 0;
    private float spawnPeriod;
    private int count;

    // Properti dasar untuk gelombang musuh saat ini.
    private float health;
    private int enemyNumberLimit;
    private int bounty;
    // "GPS" atau daftar arah yang didapat dari Map.
    private final LinkedList<Direction> directionList;

    // Timer untuk jeda antar gelombang.
    private int nextWaveTimer = 0;

    // Referensi ke Level agar bisa berkomunikasi (misal: mengurangi nyawa pemain).
    private final Level level;

    // Kecepatan musuh untuk gelombang ini.
    private int enemySpeed = GameConstants.ENEMY_SPEED;

    // Daftar untuk animasi koin yang muncul saat musuh mati.
    private final List<Bounty> bountyList;

    // Constructor, mengambil data awal dari Level.
    public EnemyController(Level level) {
        this.level = level;
        Map map = level.getMap();
        this.directionList = map.getDirectionList();
        this.health = level.getEnemyHealth();
        this.enemyNumberLimit = level.getEnemyNumber();

        count = level.getEnemyNumber();
        enemyList = new ArrayList<>();
        bountyList = new ArrayList<>();

        this.bounty = GameConstants.ENEMY_BOUNTY;
        this.spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD;
    }

    // Meneruskan perintah render (sprite) ke setiap musuh dan koin.
    public void render(SpriteBatch sb) {
        for (Enemy enemy : enemyList) {
            enemy.render(sb);
        }
        for (Bounty bounty : bountyList) {
            bounty.render(sb);
        }
    }

    // Meneruskan perintah render (bentuk) ke setiap musuh.
    public void render(ShapeRenderer sr) {
        for (Enemy enemy : enemyList) {
            enemy.render(sr);
        }
    }

    // Metode update utama, dipanggil setiap frame.
    public void update(float deltaTime) {
        // Update setiap musuh dan koin.
        for (Enemy enemy : enemyList) {
            enemy.update(deltaTime);
        }
        for (Bounty bounty : bountyList) {
            bounty.update(deltaTime);
        }
        // Menjalankan semua logika penting.
        removeCoins();
        createEnemy();
        removeDeadEnemies();
        checkIfEnemyOfScreen();
    }

    // Logika untuk memunculkan musuh baru secara berkala.
    public void createEnemy() {
        spawnTime += Gdx.graphics.getDeltaTime();
        if (spawnTime >= spawnPeriod) {
            spawnTime = 0;
            if (count < enemyNumberLimit) {
                Vector2 p = new Vector2(0, 0);
                enemyList.add(new Enemy(
                        p.x - GameConstants.GRID_WIDTH + (GameConstants.GRID_WIDTH / 2 - GameConstants.ENEMY_WIDTH / 2),
                        p.y + GameConstants.GRID_HEIGHT
                                + (GameConstants.GRID_HEIGHT / 2 - GameConstants.ENEMY_HEIGHT / 2),
                        GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT, health, directionList, bounty,
                        enemySpeed));
                count++;
            } else {
                // Jika semua musuh di gelombang ini sudah muncul, cek status gelombang.
                isWaveCompleted();
            }
        }
    }

    // Logika untuk memulai gelombang berikutnya setelah gelombang saat ini bersih.
    private void isWaveCompleted() {
        // Jika tidak ada musuh tersisa di layar.
        if (enemyList.isEmpty()) {
            nextWaveTimer++;
            if (nextWaveTimer % 2 == 0)
                level.nextWaveCountDown((GameConstants.NEXT_WAVE_SPAWN_TIME / 2 - nextWaveTimer / 2));

            // Jika waktu tunggu sudah habis, siapkan gelombang baru.
            if (nextWaveTimer == GameConstants.NEXT_WAVE_SPAWN_TIME) {
                count = 0;
                nextWaveTimer = 0;
                this.health *= 2; // Tingkatkan health musuh.
                this.enemyNumberLimit += 1;
                this.bounty += 7;
                level.newWaveCreated(this.enemyNumberLimit);
            }
        }
    }

    // Memeriksa jika ada musuh yang berhasil lolos (keluar dari layar).
    private void checkIfEnemyOfScreen() {
        List<Enemy> shouldRemoved = new ArrayList<>();
        for (Enemy e : enemyList) {
            if (e.position.x + e.size.x > GameConstants.VIRTUAL_WIDTH) {
                shouldRemoved.add(e);
                level.enemyPassedTheCheckPoint(); // Beritahu Level bahwa nyawa pemain berkurang.
            }
        }
        enemyList.removeAll(shouldRemoved);
    }

    // Menghapus animasi koin yang sudah selesai.
    private void removeCoins() {
        List<Bounty> bounties = new ArrayList<>();
        for (Bounty b : bountyList) {
            if (b.isDisposable()) {
                bounties.add(b);
            }
        }
        bountyList.removeAll(bounties);
    }

    // Menghapus musuh yang sudah mati dari daftar.
    public void removeDeadEnemies() {
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy e = enemyList.get(i);
            if (!e.isAlive()) {
                level.enemyKilled(e.getBounty()); // Beritahu Level untuk menambah uang & skor.
                enemyList.remove(i);
                // Tambahkan animasi koin di posisi musuh mati.
                bountyList.add(
                        new Bounty(e.position.x, e.position.y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT));
                i--;
            }
        }
    }

    // Getter untuk memberikan daftar musuh ke objek lain (misalnya Tower).
    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    // Mengatur kecepatan saat mode 2x aktif.
    public void speed2xClicked() {
        enemySpeed = GameConstants.ENEMY_SPEED * 2;
        spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD / 2;
        for (Enemy enemy : enemyList) {
            enemy.speed2XClicked();
        }
    }

    // Mengembalikan kecepatan ke normal.
    public void normalSpeedClicked() {
        enemySpeed = GameConstants.ENEMY_SPEED;
        spawnPeriod = GameConstants.ENEMY_SPAWN_PERIOD;
        for (Enemy enemy : enemyList) {
            enemy.normalSpeedClicked();
        }
    }
}