package com.reus.algorithms1.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF grid;
    private final WeightedQuickUnionUF backwashGrid;
    private final int TOP_SIDE = 0;
    private final int BOT_SIDE;
    private final int n;
    private final boolean[] opened;
    private int openedCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        grid = new WeightedQuickUnionUF(n * n + 2);
        backwashGrid = new WeightedQuickUnionUF(n * n + 2);
        BOT_SIDE = n * n + 1;
        opened = new boolean[n * n];
    }

    // test client (optional)
    public static void main(String[] args) {

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        int openedNode = getNodeNum(row, col);
        if (opened[openedNode - 1]) {
            return;
        }
        opened[openedNode - 1] = true;
        openedCount++;
        int topNode = getNodeNum(row - 1, col);
        if (topNode != -1 && opened[topNode - 1]) {
            grid.union(openedNode, topNode);
            backwashGrid.union(openedNode, topNode);
        }
//        System.out.println(String.format("Open, row=%d, col=%d, node=%d", row, col, openedNode));
        if (row == 1) {
            grid.union(openedNode, TOP_SIDE);
            backwashGrid.union(openedNode, TOP_SIDE);
        }
        if (row == n) {
            grid.union(openedNode, BOT_SIDE);
        }
        int downNode = getNodeNum(row + 1, col);
        if (downNode != -1 && opened[downNode - 1]) {
            grid.union(openedNode, downNode);
            backwashGrid.union(openedNode, downNode);
        }
        int leftNode = getNodeNum(row, col - 1);
        if (leftNode != -1 && opened[leftNode - 1]) {
            grid.union(openedNode, leftNode);
            backwashGrid.union(openedNode, leftNode);
        }
        int rightNode = getNodeNum(row, col + 1);
        if (rightNode != -1 && opened[rightNode - 1]) {
            grid.union(openedNode, rightNode);
            backwashGrid.union(openedNode, rightNode);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return opened[getNodeNum(row, col) - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        int node = getNodeNum(row, col);
        return backwashGrid.find(node) == backwashGrid.find(TOP_SIDE);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.find(BOT_SIDE) == grid.find(TOP_SIDE);
    }

    // starting from 1
    private int getNodeNum(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            return -1;
        }
        int result = (row - 1) * n + col;
        if (result < 0) {
            result = 0;
        }
        return result;
    }
}
