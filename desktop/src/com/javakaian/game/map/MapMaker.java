package com.javakaian.game.map;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.util.GameConstants;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class MapMaker {

    // Bertugas merancang denah jalur secara acak.
    private int rowSize;
    private int columnSize;
    private int[][] matrix; // Representasi peta dalam bentuk angka.
    private final Set<Vector2> pathPoints; // Kumpulan koordinat semua titik jalur.
    private final LinkedList<Direction> directionList; // Daftar perintah arah untuk musuh (GPS).

    // Constructor.
    public MapMaker() {
        directionList = new LinkedList<>();
        pathPoints = new HashSet<>();
        initialize();
    }

    // Menjalankan semua proses pembuatan peta.
    public void initialize() {
        columnSize = GameConstants.COLUMN_SIZE;
        rowSize = GameConstants.MAP_ROW_SIZE;
        matrix = generateMap(rowSize, columnSize); // Buat denah dasar.
        loadPathPoints(); // Kumpulkan titik jalur.
        loadDirectionList(0, 0); // Buat daftar arah.

        // Menambahkan arah awal dan akhir untuk musuh.
        directionList.add(0, Direction.RIGHT);
        directionList.add(directionList.get(directionList.size() - 1));
    }

    // Menelusuri jalur di matriks dan mengubahnya menjadi daftar arah (UP, DOWN, etc).
    public void loadDirectionList(int x, int y) {
        matrix[x][y] = 0;
        if (!(x + 1 >= rowSize) && matrix[x + 1][y] == 1) {
            matrix[x + 1][y] = 0;
            directionList.add(Direction.DOWN);
            loadDirectionList(x + 1, y);
        } else if (!(x - 1 < 0) && matrix[x - 1][y] == 1) {
            matrix[x - 1][y] = 0;
            directionList.add(Direction.UP);
            loadDirectionList(x - 1, y);
        } else if (!(y + 1 >= columnSize) && matrix[x][y + 1] == 1) {
            matrix[x][y + 1] = 0;
            directionList.add(Direction.RIGHT);
            loadDirectionList(x, y + 1);
        }
    }

    // Mengisi bagian dari matriks dengan angka 1 untuk membuat jalur.
    public void fillMap(int col, int start, int finish) {
        if (start > finish) {
            int temp = finish;
            finish = start;
            start = temp;
        }
        for (int i = start; i < finish + 1; i++) {
            matrix[i][col] = 1;
        }
    }

    // Membuat matriks 2D dan mengisi jalur dengan angka 1 secara acak.
    public int[][] generateMap(int row, int col) {
        matrix = new int[row][col];
        int start = 0;
        int finish = 0;
        Random rand = new Random();

        for (int i = 0; i < col; i++) {
            if (i % 2 == 0) {
                finish = rand.nextInt(row);
                fillMap(i, start, finish);
                start = finish;
            } else {
                fillMap(i, start, finish);
            }
        }
        return matrix;
    }

    // Mengumpulkan semua titik jalur dari matriks ke dalam Set.
    public void loadPathPoints() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (matrix[i][j] == 1) {
                    pathPoints.add(new Vector2(i, j));
                }
            }
        }
    }

    // Getter untuk mengambil blueprint jalur.
    public Set<Vector2> getPathPoints() {
        return pathPoints;
    }

    // Getter untuk mengambil "GPS" musuh.
    public LinkedList<Direction> getDirectionList() {
        return directionList;
    }

    // Enum untuk merepresentasikan empat arah mata angin.
    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}