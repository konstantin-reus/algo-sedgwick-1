package com.reus.algorithms1.percolation;

public class PercolationOnArray {
    private static final int TOP_SIDE = 0;
    private static final int CLOSED = -1;
    private final int[][] grid;
    private final int[][] backWashGrid;
    private final int n;
    private final int BOT_SIDE_NODE_NUM;
    private int openSites = 0;
    private int[] size;
    private int[] backwashSize;

    public PercolationOnArray(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new int[n + 2][n];
        backWashGrid = new int[n + 2][n];
        size = new int[(n + 2) * n];
        backwashSize = new int[(n + 2) * n];
        for (int i = 0; i < n; i++) {
            size[i] = 0;
            backwashSize[i] = 0;
        }
        for (int i = n; i < n * n + n; i++) {
            size[i] = 1;
            backwashSize[i] = 1;
        }
        for (int i = n * n + n; i < (n + 2) * n; i++) {
            size[i] = 0;
            backwashSize[i] = 0;
        }
        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = CLOSED;
                backWashGrid[i][j] = CLOSED;
            }
        }
        this.n = n;
        grid[0][0] = TOP_SIDE;
        backWashGrid[0][0] = TOP_SIDE;
        BOT_SIDE_NODE_NUM = (n * n) + 1;
        grid[n + 1][0] = BOT_SIDE_NODE_NUM;
    }

    /**
     * opens the site (row, col) if it is not open already
     *
     * @param row row starting from 1
     * @param col col starting from 1
     */
    public void open(final int row, final int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        if (grid[row][col - 1] != CLOSED) {
            return;
        }
        int openedNode = getNodeNum(row, col, n);
//        System.out.println(String.format("Open, row=%d, col=%d, node=%d", row, col, openedNode));
        grid[row][col - 1] = openedNode;
        backWashGrid[row][col - 1] = openedNode;
        openSites++;
        if (row == 1) {
            union(openedNode, TOP_SIDE);
        } else {
            int topNode = getNodeNum(row - 1, col, n);
            union(openedNode, topNode);
        }
        if (row == n) {
            union(openedNode, BOT_SIDE_NODE_NUM);
        } else {
            int downNode = getNodeNum(row + 1, col, n);
            union(openedNode, downNode);
        }
        if (col > 1) {
            int leftNode = getNodeNum(row, col - 1, n);
            union(openedNode, leftNode);
        }
        if (col < n) {
            int rightNode = getNodeNum(row, col + 1, n);
            union(openedNode, rightNode);
        }
    }

    // starting from 1
    private int getNodeNum(int row, int col, int n) {
        int result = (row - 1) * n + col;
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    private void union(int p, int q) {
        int[] rowColP = getRowCol(p);
        int[] rowColQ = getRowCol(q);
        if (grid[rowColP[0]][rowColP[1]] == CLOSED || grid[rowColQ[0]][rowColQ[1]] == CLOSED) {
//            System.out.println(String.format("grid[%d][%d] == %d, grid[%d][%d] == %d (CLOSED)",
//                    rowColP[0],
//                    rowColP[1],
//                    grid[rowColP[0]][rowColP[1]],
//                    rowColQ[0],
//                    rowColQ[1],
//                    grid[rowColQ[0]][rowColQ[1]]));
            return;
        }
//        System.out.println(String.format("Union p=%d, q=%d", p, q));
        int rootP = find(p);
        int[] rowColRootP = getRowCol(rootP);
        int rootQ = find(q);
        int[] rowColRootQ = getRowCol(rootQ);
        backwashUnion(p, q);
        if (rootP == rootQ) {
            return;
        }
        int rootPsize = getNodeTreeSize(rootP);
        int rootQsize = getNodeTreeSize(rootQ);
//        System.out.println(String.format("Size(root(%d)) == %d, size(root(%d)) == %d", p, rootPsize,
//                q, rootQsize));
        if (rootPsize < rootQsize) {
//            System.out.println(String.format("Set grid[%d][%d] (value=%d) = grid[%d][%d] (value %d)",
//                    rowColRootP[0],
//                    rowColRootP[1],
//                    grid[rowColRootP[0]][rowColRootP[1]],
//                    rowColRootQ[0],
//                    rowColRootQ[1],
//                    grid[rowColRootQ[0]][rowColRootQ[1]]));
            grid[rowColRootP[0]][rowColRootP[1]] = grid[rowColRootQ[0]][rowColRootQ[1]];
            updateTreeSize(rootQ, rootPsize);
        } else {
//            System.out.println(String.format("Set grid[%d][%d] (value=%d) = grid[%d][%d] (value %d)",
//                    rowColRootQ[0],
//                    rowColRootQ[1],
//                    grid[rowColRootQ[0]][rowColRootQ[1]],
//                    rowColRootP[0],
//                    rowColRootP[1],
//                    grid[rowColRootP[0]][rowColRootP[1]]));
            grid[rowColRootQ[0]][rowColRootQ[1]] = grid[rowColRootP[0]][rowColRootP[1]];
            updateTreeSize(rootP, rootQsize);
        }
    }

    private void backwashUnion(int p, int q) {
//        System.out.println(String.format("Backwash union p=%d, q=%d", p, q));
        if (p == BOT_SIDE_NODE_NUM || q == BOT_SIDE_NODE_NUM) {
//            System.out.println("No union");
            return;
        }
        int rootP = backwashFind(p);
        int rootQ = backwashFind(q);
        if (rootP == rootQ) {
//            System.out.println(String.format("Backwash root(%d) == root(%d) == %d", p, q, rootP));
            return;
        }
        int[] rowColRootP = getRowCol(rootP);
        int[] rowColRootQ = getRowCol(rootQ);
        if (backWashGrid[rowColRootP[0]][rowColRootP[1]] == CLOSED || backWashGrid[rowColRootQ[0]][rowColRootQ[1]] == CLOSED) {
//            System.out.println(String.format("backwashGrid[%d][%d] == %d, backwashGrid[%d][%d] == %d (CLOSED)",
//                    rowColRootP[0],
//                    rowColRootP[1],
//                    backWashGrid[rowColRootP[0]][rowColRootP[1]],
//                    rowColRootQ[0],
//                    rowColRootQ[1],
//                    backWashGrid[rowColRootQ[0]][rowColRootQ[1]]));
            return;
        }
        int rootPsize = getBackwashTreeSize(rootP);
        int rootQsize = getBackwashTreeSize(rootQ);
//        System.out.println(String.format("Backwash size(root(%d)) == %d, Backwash size(root(%d)) == %d", p, rootPsize,
//                q, rootQsize));
        if (rootPsize < rootQsize) {
//            System.out.println(String.format("Set backWashGrid[%d][%d] (value=%d) = backWashGrid[%d][%d] (value %d)",
//                    rowColRootP[0],
//                    rowColRootP[1],
//                    backWashGrid[rowColRootP[0]][rowColRootP[1]],
//                    rowColRootQ[0],
//                    rowColRootQ[1],
//                    backWashGrid[rowColRootQ[0]][rowColRootQ[1]]));
            backWashGrid[rowColRootP[0]][rowColRootP[1]] = backWashGrid[rowColRootQ[0]][rowColRootQ[1]];
            updateBackwashTreeSize(rootQ, rootPsize);
        } else {
//            System.out.println(String.format("Set backWashGrid[%d][%d] (value=%d) = backWashGrid[%d][%d] (value %d)",
//                    rowColRootQ[0],
//                    rowColRootQ[1],
//                    backWashGrid[rowColRootQ[0]][rowColRootQ[1]],
//                    rowColRootP[0],
//                    rowColRootP[1],
//                    backWashGrid[rowColRootP[0]][rowColRootP[1]]));
            backWashGrid[rowColRootQ[0]][rowColRootQ[1]] = backWashGrid[rowColRootP[0]][rowColRootP[1]];
            updateBackwashTreeSize(rootP, rootQsize);
        }
    }


    private void updateTreeSize(int root, int toAdd) {
//        System.out.println(String.format("Size(%d) (current = %d) += size(%d) (current=%d)",
//                root,
//                rootCurSize,
//                addedNode,
//                toAdd));
        if (root == TOP_SIDE) {
            size[0] += toAdd;
        } else if (root == BOT_SIDE_NODE_NUM) {
            size[(n * n) + 1] += toAdd;
        } else {
            size[root - 1 + n] += toAdd;
        }
    }

    private void updateBackwashTreeSize(int root, int toAdd) {
//        System.out.println(String.format("Size(%d) (current = %d) += size(%d) (current=%d)",
//                root,
//                rootCurSize,
//                addedNode,
//                toAdd));
        if (root == TOP_SIDE) {
            backwashSize[0] += toAdd;
        } else if (root == BOT_SIDE_NODE_NUM) {
            backwashSize[(n * n) + 1] += toAdd;
        } else {
            backwashSize[root - 1 + n] += toAdd;
        }
    }

    private int getNodeTreeSize(int node) {
        if (node == TOP_SIDE) {
            return size[0];
        } else if (node == BOT_SIDE_NODE_NUM) {
            return size[(n * n) + 1];
        } else {
            return size[node - 1 + n];
        }
    }

    private int getBackwashTreeSize(int node) {
        if (node == TOP_SIDE) {
            return backwashSize[0];
        } else if (node == BOT_SIDE_NODE_NUM) {
            return backwashSize[(n * n) + 1];
        } else {
            return backwashSize[node - 1 + n];
        }
    }

    private int[] getRowCol(int node) {
        if (node == TOP_SIDE) {
            return new int[]{0, 0};
        } else if (node == BOT_SIDE_NODE_NUM) {
            return new int[]{n + 1, 0};
        }
        int[] result = new int[2];
        int row = ((node - 1) / n) + 1;
        int col = (node - 1) % n < 0 ? 0 : (node - 1) % n;
        result[0] = row;
        result[1] = col;
        return result;
    }

    private int find(int node) {
//        System.out.println(String.format("Find root of %d", node));
        int[] rowCol = getRowCol(node); // node = 7, row = 2, col = 1
        int p = grid[rowCol[0]][rowCol[1]];
        while (p != getNodeNum(rowCol[0], rowCol[1] + 1, n)) {
            rowCol = getRowCol(p);
            p = grid[rowCol[0]][rowCol[1]];
        }
//        System.out.println(String.format("Root of %d is %d", node, p));
        return p;
    }

    private int backwashFind(int node) {
//        System.out.println(String.format("BackwashFind root of %d", node));
        int[] rowCol = getRowCol(node);
        int p = backWashGrid[rowCol[0]][rowCol[1]];
        while (p != getNodeNum(rowCol[0], rowCol[1] + 1, n)) {
            rowCol = getRowCol(p);
            p = backWashGrid[rowCol[0]][rowCol[1]];
        }
//        System.out.println(String.format("Backwash root of %d is %d", node, p));
        return p;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return grid[row][col - 1] != CLOSED;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
//        System.out.println(String.format("isFull ? row=%d, col=%d", row, col));
        if (grid[row][col - 1] == CLOSED) {
//            System.out.println(String.format("row=%d, col=%d is closed", row, col));
            return false;
        }
        int node = getNodeNum(row, col, n);
        return backWashConnected(node, TOP_SIDE);
    }

    private boolean backWashConnected(int p, int q) {
//        System.out.println(String.format("Backwash connected ? p=%d, q=%d", p, q));
//        System.out.println(String.format("Backwash connected ? p=%d, q=%d, result=%s", p, q, result));
        return backwashFind(p) == backwashFind(q);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return connected(TOP_SIDE, BOT_SIDE_NODE_NUM);
    }

    private boolean connected(int p, int q) {
//        System.out.println(String.format("connected ? p=%d, q=%d", p, q));
        if (p == CLOSED || q == CLOSED) {
//            System.out.println("false");
            return false;
        }
        return find(p) == find(q);
    }
}
