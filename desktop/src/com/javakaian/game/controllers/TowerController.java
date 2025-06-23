package com.javakaian.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.entity.Enemy;
import com.javakaian.game.towers.BaseTower;
import com.javakaian.game.towers.BaseTower.TowerType;
import com.javakaian.game.towers.ElectricTower;
import com.javakaian.game.towers.FireTower;
import com.javakaian.game.towers.IceTower;
import com.javakaian.game.util.GameConstants;

import java.util.ArrayList;
import java.util.List;

// Kelas ini bertugas untuk mengelola semua tower yang ada di dalam game.
public class TowerController {

    // Daftar untuk menyimpan semua objek tower yang aktif di peta.
    private final List<BaseTower> towerList;

    // Referensi ke tower yang sedang dipilih oleh pemain.
    private BaseTower selectedTower;

    // Penanda apakah game sedang dalam mode kecepatan 2x.
    private boolean speedMode = false;

    // Menghapus tower dari daftar (misalnya saat dijual).
    public void removeTower(BaseTower tower) {
        if (tower != null) {
            towerList.remove(tower);
            if (selectedTower == tower) {
                clearSelectedTower();
            }
        }
    }

    // Constructor, inisialisasi daftar tower.
    public TowerController() {
        towerList = new ArrayList<>();
    }

    // Memanggil metode update untuk setiap tower di dalam daftar.
    public void update(float deltaTime) {
        for (BaseTower tower : towerList) {
            tower.update(deltaTime);
        }
    }

    // Meneruskan perintah render (untuk bentuk) ke setiap tower.
    public void render(ShapeRenderer sr) {
        for (BaseTower tower : towerList) {
            tower.render(sr);
        }
    }

    // Meneruskan perintah render (untuk sprite/gambar) ke setiap tower.
    public void render(SpriteBatch sb) {
        for (BaseTower tower : towerList) {
            tower.render(sb);
        }
    }

    /** Logika utama untuk membangun tower.
     * Mengecek kecukupan uang dan memanggil metode build yang sesuai.
     * Mengembalikan biaya tower jika berhasil, atau 0 jika gagal.
     */
    public int buildTower(float x, float y, List<Enemy> enemyList, TowerType type, int money) {
        int cost;
        switch (type) {
            case FIRE:
                cost = GameConstants.TOWER_PRICE;
                if (money >= cost) {
                    return buildFireTower(x, y, enemyList);
                }
                break;
            case ICE:
                cost = GameConstants.TOWER_PRICE;
                if (money >= cost) {
                    return buildIceTower(x, y, enemyList);
                }
                break;
            case ELECTRIC:
                cost = GameConstants.ELECTRIC_TOWER_PRICE;
                if (money >= cost) {
                    return buildElectricTower(x, y, enemyList);
                }
                break;
        }
        return 0; // Gagal membangun tower.
    }

    // Metode privat untuk membuat objek FireTower baru.
    private int buildFireTower(float x, float y, List<Enemy> enemyList) {
        FireTower ft = new FireTower(x, y, enemyList);
        if (speedMode) {
            ft.setSpeed(ft.getSpeed() * 2);
        }
        towerList.add(ft);
        return GameConstants.TOWER_PRICE;
    }

    // Metode privat untuk membuat objek IceTower baru.
    private int buildIceTower(float x, float y, List<Enemy> enemyList) {
        IceTower it = new IceTower(x, y, enemyList);
        if (speedMode) {
            it.setSpeed(it.getSpeed() * 2);
        }
        towerList.add(new IceTower(x, y, enemyList));
        return GameConstants.TOWER_PRICE;
    }

    // Metode privat untuk membuat objek ElectricTower baru.
    private int buildElectricTower(float x, float y, List<Enemy> enemyList) {
        ElectricTower et = new ElectricTower(x, y, enemyList);
        if (speedMode) {
            et.setDamage(et.getDamage() * 2);
        }
        towerList.add(new ElectricTower(x, y, enemyList));
        return GameConstants.ELECTRIC_TOWER_PRICE;
    }

    // Memilih tower berdasarkan posisi (biasanya dari klik mouse).
    public BaseTower getSelectedTower(Vector2 center) {
        for (BaseTower tower : towerList) {
            tower.setSelected(false); // Hapus seleksi dari tower lain.
            if (tower.position.equals(center)) {
                selectedTower = tower;
                selectedTower.setSelected(true);
            }
        }
        return selectedTower;
    }

    // Mengambil objek tower yang sedang dipilih.
    public BaseTower getSelectedTower() {
        return selectedTower;
    }

    // Meneruskan perintah upgrade serangan ke tower yang dipilih.
    public void increaseAttack() {
        selectedTower.increaseDamage();
    }

    // Meneruskan perintah upgrade jangkauan ke tower yang dipilih.
    public void increaseRange() {
        selectedTower.increaseRange();
    }

    // Meneruskan perintah upgrade kecepatan ke tower yang dipilih.
    public void increaseSpeed() {
        selectedTower.increaseSpeed();
    }

    // Menghapus status "terpilih" dari tower manapun.
    public void clearSelectedTower() {
        if (selectedTower != null) {
            selectedTower.setSelected(false);
            selectedTower = null;
        }
    }

    // Mengaktifkan mode kecepatan 2x untuk semua tower.
    public void speed2xClicked() {
        speedMode = true;
        for (BaseTower baseTower : towerList) {
            baseTower.setSpeed(baseTower.getSpeed() * 2);
        }
    }

    // Mengembalikan kecepatan semua tower ke normal.
    public void normalSpeedClicked() {
        speedMode = false;
        for (BaseTower baseTower : towerList) {
            baseTower.setSpeed(baseTower.getSpeed() / 2);
        }
    }
}