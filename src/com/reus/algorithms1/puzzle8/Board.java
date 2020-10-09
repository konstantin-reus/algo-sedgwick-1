package com.reus.algorithms1.puzzle8;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        n = tiles[0].length;
    }

    private static Board move(int[][] tiles, Pair moveFrom, Pair moveTo, boolean isZeroSwitch) {
        int[][] newTiles = new int[tiles.length][tiles[0].length];
        int moved = isZeroSwitch
                ? 0
                : tiles[moveTo.getI()][moveTo.getJ()];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (i == moveFrom.getI() && j == moveFrom.getJ()) {
                    newTiles[i][j] = moved;
                } else if (i == moveTo.getI() && j == moveTo.getJ()) {
                    newTiles[i][j] = tiles[moveFrom.getI()][moveFrom.getJ()];
                } else {
                    newTiles[i][j] = tiles[i][j];
                }
            }
        }
        return new Board(newTiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result
                .append(tiles.length)
                .append('\n');
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                result.append(tiles[i][j]);
                if (j != n - 1) {
                    result.append(' ');
                }
            }
            result.append('\n');
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int counter = 1;
        int wrong = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (i == n - 1 && j == n - 1) {
                    return tiles[i][j] == 0
                            ? wrong
                            : wrong + 1;
                }
                if (tiles[i][j] != counter++) {
                    wrong++;
                }
            }
        }
        return wrong;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int totalManhattan = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Pair expectedCoordinates = getExpectedCoordinates(tiles[i][j]);
                int diffI = Math.abs(i - expectedCoordinates.getI());
                int diffJ = Math.abs(j - expectedCoordinates.getJ());
                totalManhattan += diffI + diffJ;
            }
        }
        return totalManhattan;
    }

    private Pair getExpectedCoordinates(int i) {
        if (i == 0) {
            return new Pair(n - 1, n - 1);
        }
        int targetI = i / n;
        int targetJ = (i - 1) % n;
        return new Pair(targetI, targetJ);
    }

    // is this board the goal board?
    public boolean isGoal() {
        int counter = 1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (i == tiles.length - 1 && j == tiles[i].length - 1) {
                    return tiles[i][j] == 0;
                } else if (tiles[i][j] != counter++) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (!(y instanceof Board)) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) {
            return false;
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Board[] boards = new Board[4];
        int c = 0;
        List<Board> result = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != 0) {
                    continue;
                }
                if (i - 1 >= 0) { //move up -> down (to 0)
                    Board upBoard = move(tiles, new Pair(i - 1, j), new Pair(i, j), true);
                    result.add(upBoard);
                }
                if (i + 1 < tiles.length) { //move down -> up (to 0)
                    Board downBoard = move(tiles, new Pair(i + 1, j), new Pair(i, j), true);
                    result.add(downBoard);
                }
                if (j - 1 >= 0) { //move left -> right (to 0)
                    Board leftBoard = move(tiles, new Pair(i, j - 1), new Pair(i, j), true);
                    result.add(leftBoard);
                }
                if (j + 1 < tiles[i].length) { //move right -> left (to 0)
                    Board rightBoard = move(tiles, new Pair(i, j + 1), new Pair(i, j), true);
                    result.add(rightBoard);
                }
            }
        }
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int randomI = StdRandom.uniform(tiles.length);
        int randomJ = StdRandom.uniform(tiles[randomI].length);
        if (randomJ - 1 >= 0) { //move left -> right (to 0)
            return move(tiles, new Pair(randomI, randomJ - 1), new Pair(randomI, randomJ), false);
        } else {
            return move(tiles, new Pair(randomI, randomJ + 1), new Pair(randomI, randomJ), false);
        }
    }

    private static class Pair {
        private final int i;
        private final int j;

        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }
    }

}
