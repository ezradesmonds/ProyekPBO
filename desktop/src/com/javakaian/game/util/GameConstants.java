package com.javakaian.game.util;

// Kelas ini menyimpan semua nilai konstan yang digunakan dalam game.
// Tujuannya agar mudah untuk mengubah balancing dan pengaturan game dari satu tempat.
public class GameConstants {

	// Warna dasar untuk latar belakang menu dan game.
	public static final float RED = 50f / 255f;
	public static final float GREEN = 63f / 255f;
	public static final float BLUE = 94f / 255f;
	public static final float ALPHA = 0.5f;

	// Skala permainan, saat ini tidak diubah (100%).
	public static float GAME_SCALE = 1f;

	// Pengaturan ukuran grid dan peta.
	public static int ROW_SIZE = 9; // Total baris layar.
	public static int MAP_ROW_SIZE = 6; // Baris yang digunakan untuk area permainan.
	public static int COLUMN_SIZE = 16; // Total kolom layar.

	// Ukuran dunia virtual untuk menjaga rasio aspek 16:9 (responsif).
	public static final float VIRTUAL_HEIGHT = 720;
	public static final float VIRTUAL_WIDTH = (16 * VIRTUAL_HEIGHT) / 9;

	// Ukuran satu petak/grid, dihitung dari ukuran virtual.
	public static float GRID_WIDTH = VIRTUAL_WIDTH / COLUMN_SIZE;
	public static float GRID_HEIGHT = VIRTUAL_HEIGHT / ROW_SIZE;

	// Pengaturan dasar untuk musuh (Enemy).
	public static float ENEMY_WIDTH = GRID_WIDTH / 2f;
	public static float ENEMY_HEIGHT = GRID_HEIGHT / 2f;
	public static float ENEMY_SPAWN_PERIOD = 0.8f; // Jeda antar musuh muncul (detik).
	public static int ENEMY_SPEED = (int) GRID_WIDTH; // Kecepatan gerak musuh.

	// Pengaturan dasar untuk menara (Tower).
	public static float TOWER_SIZE = GRID_WIDTH;
	public static float TOWER_RANGE = GRID_WIDTH * 2;
	public static int TOWER_DAMAGE_FIRE = 90;
	public static int TOWER_DAMAGE_ICE = 1;
	public static int TOWER_DAMAGE_ELECTRIC = 10;

	// Pengaturan ekonomi dan aturan main awal.
	public static int INITIAL_MONEY = 1500; // Uang awal pemain.
	public static int SCORE_INCREASE_CONSTANT = 100; // Skor per musuh.
	public static int ENEMY_BOUNTY = 2; // Uang per musuh.
	public static int NEXT_WAVE_SPAWN_TIME = 10; // Jeda antar gelombang (detik).
	public static int REMAINING_HEALTH = 6; // Nyawa awal pemain.

	// Ukuran untuk elemen-elemen UI.
	public static float MENU_ITEM_WIDTH = GRID_WIDTH * (1.5f);
	public static float MENU_ITEM_HEIGHT = GRID_HEIGHT * (1.5f);
	public static float FUNC_BUTTON_WH = GRID_WIDTH * (0.8f);

	// Pengaturan dasar untuk peluru (Bullet).
	public static float BULLET_WIDTH = GRID_WIDTH / 2;
	public static float BULLET_HEIGHT = GRID_HEIGHT / 2;
	public static float BULLET_SPEED = GRID_WIDTH * 8;

	// Harga untuk membangun dan upgrade tower.
	public static int TOWER_PRICE = 50;
	public static int ELECTRIC_TOWER_PRICE = 200;
	public static int TOWER_RANGE_PRICE = 15;
	public static int TOWER_SPEED_PRICE = 15;
	public static int TOWER_ATTACK_PRICE = 15;
}