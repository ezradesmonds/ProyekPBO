package com.javakaian.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.map.MapMaker.Direction;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.util.GameConstants;

import java.util.LinkedList;

// Kelas Enemy merepresentasikan satu unit musuh dalam game.
// Mewarisi sifat dasar dari GameObject.
public class Enemy extends GameObject {

    // Sisa nyawa musuh.
    private float remainingHealth;
    // Uang yang didapat saat musuh ini dikalahkan.
    private final int bounty;
    // Kecepatan gerak musuh.
    private int speed;

    // Jarak yang tersisa untuk ditempuh di petak saat ini sebelum ganti arah.
    private float distanceToTile;
    // Arah gerak musuh saat ini.
    private Direction currentDirection;

    // Daftar urutan arah yang harus diikuti musuh (seperti GPS).
    private final LinkedList<Direction> directionList;
    // Status hidup (true) atau mati (false) dari musuh.
    private boolean alive = true;

    // Waktu untuk menghitung durasi efek slow.
    private float slowDownTime;
    // Status apakah musuh sedang dalam efek slow.
    private boolean slowed = false;

    // Objek HealthBar yang dimiliki oleh musuh ini (Contoh Komposisi).
    private final HealthBar healthBar;

    // Constructor untuk menciptakan objek musuh.
    public Enemy(float x, float y, float width, float height, float health, LinkedList<Direction> directionList,
                 int bounty, int speed) {
        super(x, y, width, height); // Memanggil constructor dari GameObject.
        this.speed = speed;
        // Membuat salinan dari directionList agar tidak mengganggu list aslinya.
        this.directionList = new LinkedList<>(directionList);
        this.remainingHealth = health;
        this.bounty = bounty;
        this.sprite = MyAtlas.ENEMY;
        // Mengambil arah pertama saat musuh diciptakan.
        getNextDirection();

        // Membuat objek HealthBar yang terikat pada musuh ini.
        healthBar = new HealthBar(x, y - height / 5, width, height / 5, remainingHealth);
    }

    // Menggambar komponen non-gambar seperti HealthBar.
    @Override
    public void render(ShapeRenderer sr) {
        healthBar.render(sr);
    }

    // Menggambar sprite (gambar) musuh ke layar.
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    // Metode ini dipanggil setiap frame untuk memperbarui logika dan posisi musuh.
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (currentDirection == null) {
            return;
        }
        // Jika musuh sudah sampai di ujung petak, ambil arah baru.
        if (distanceToTile < 0) {
            getNextDirection();
        }
        // Jika masih ada arah yang harus dituju, gerakkan musuh.
        if (currentDirection != null) {
            float distance = speed * deltaTime;
            switch (currentDirection) {
                case UP:
                    position.y -= distance;
                    distanceToTile -= distance;
                    if (distanceToTile < 0) {
                        position.y -= distanceToTile;
                    }
                    break;
                case DOWN:
                    position.y += distance;
                    distanceToTile -= distance;
                    if (distanceToTile < 0) {
                        position.y += distanceToTile;
                    }
                    break;
                case LEFT:
                    position.x -= distance;
                    distanceToTile -= distance;
                    if (distanceToTile < 0) {
                        position.x -= distanceToTile;
                    }
                    break;
                case RIGHT:
                    position.x += distance;
                    distanceToTile -= distance;
                    if (distanceToTile < 0) {
                        position.x += distanceToTile;
                    }
                    break;
                default:
                    break;
            }
        }
        // Hapus efek slow jika durasinya sudah habis.
        clearSlowEffects(deltaTime);
        // Selalu perbarui posisi health bar agar mengikuti posisi musuh.
        healthBar.position.x = position.x;
        healthBar.position.y = position.y - size.y / 5;
    }

    // Mengambil arah selanjutnya dari daftar GPS musuh.
    private void getNextDirection() {
        currentDirection = directionList.pollFirst();
        if (currentDirection != null) {
            // Mengatur jarak yang harus ditempuh berdasarkan arah.
            switch (currentDirection) {
                case UP:
                case DOWN:
                    distanceToTile = GameConstants.GRID_WIDTH;
                    break;
                case RIGHT:
                case LEFT:
                    distanceToTile = GameConstants.GRID_HEIGHT;
                    break;
            }
        }
    }

    // Metode ini dipanggil saat musuh terkena serangan.
    public void shoot(float damage) {
        this.remainingHealth -= damage;
        // Jika nyawa habis, tandai sebagai mati.
        if (this.remainingHealth <= 0) {
            alive = false;
            visible = false;
        } else {
            // Jika masih hidup, perbarui tampilan health bar.
            healthBar.setRemaniningHealth(remainingHealth);
        }
    }

    // Menghilangkan efek slow setelah durasinya berakhir.
    private void clearSlowEffects(float deltaTime) {
        if (slowed) {
            slowDownTime += deltaTime;
            float slowDownDuration = 0.5f;
            if (slowDownTime >= slowDownDuration) {
                speed *= 2; // Kembalikan kecepatan ke normal.
                sprite = MyAtlas.ENEMY;
                slowed = false;
            }
        }
    }

    // Memberikan efek slow ke musuh.
    public void slowDown() {
        // Hanya berikan efek jika belum dalam keadaan slow.
        if (!slowed) {
            slowDownTime = 0;
            speed /= 2; // Kurangi kecepatan.
            sprite = MyAtlas.ENEMY_SLOWED;
            slowed = true;
        } else {
            // Jika sudah slow, reset durasi slow.
            slowDownTime = 0;
        }
    }

    // Mengecek apakah musuh masih hidup.
    public boolean isAlive() {
        return alive;
    }

    // Mendapatkan nilai bounty dari musuh.
    public int getBounty() {
        return bounty;
    }

    // Mengatur kecepatan saat mode 2x aktif.
    public void speed2XClicked() {
        speed *= 2;
    }

    // Mengembalikan kecepatan ke normal.
    public void normalSpeedClicked() {
        speed /= 2;
    }
}