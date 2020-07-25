import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("checkstyle:Indentation")
public class Percolation {
    /**
     * ololo.
     */
    private final int[][] grid;
    /**
     * ololo.
     */
    private final Set<Integer> topIndexes = new HashSet<>();
    /**
     * ololo.
     */
    private final Set<Integer> bottomIndexes = new HashSet<>();
    /**
     * ololo.
     */
    private int openSites = 0;
    /**
     * ololo.
     */
    private int[] size;

    /**
     * creates n-by-n grid, with all sites initially blocked.
     *
     * @param n size length
     */
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new int[n][n];
        size = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            size[i] = 1;
        }
    }

    /**
     * opens the site (row, col) if it is not open already
     *
     * @param row row
     * @param col col
     */
    public void open(final int row, final int col) {
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
        if (grid[row - 1][col - 1] != 0) {
            System.out.println(String.format("row %d col %d are already opened", row, col));
            return;
        }
        int openedNode = getNodeNum(row, col, grid.length);
        if (row == 1) {
            topIndexes.add(openedNode);
        }
        System.out.println(String.format("Open row=%d, column=%d, node=%d", row, col, openedNode));

        grid[row - 1][col - 1] = openedNode;
        openSites++;
        if (row > 1) {
            int topNode = getNodeNum(row - 1, col, grid.length);
            union(openedNode, topNode);
        }
        if (row < grid.length) {
            int downNode = getNodeNum(row + 1, col, grid.length);
            union(openedNode, downNode);
        }
        if (col > 1) {
            int leftNode = getNodeNum(row, col - 1, grid.length);
            union(openedNode, leftNode);
        }
        if (col < grid.length) {
            int rightNode = getNodeNum(row, col + 1, grid.length);
            union(openedNode, rightNode);
        }
    }

    // starting from 1
    private int getNodeNum(int row, int col, int n) {
//        System.out.println(String.format("Get node number of row=%d, col=%d, n=%d", row, col, n));
        return (row - 1) * n + col;
    }

    private void union(int p, int q) {
        System.out.println(String.format("Union p=%d, q=%d", p, q));
        if (p == 0 || q == 0) {
            System.out.println("p || q == 0");
            return;
        }
        int[] rowColP = getRowCol(p);
        int[] rowColQ = getRowCol(q);
        if (grid[rowColP[0]][rowColP[1]] == 0 || grid[rowColQ[0]][rowColQ[1]] == 0) {
            return;
        }
        int rootP = findRoot(p);
        int[] rowColRootP = getRowCol(rootP);
        int rootQ = findRoot(q);
        int[] rowColRootQ = getRowCol(rootQ);
        if (rootP == rootQ) {
            return;
        }
        System.out.println(String.format("Size(root(%d)) == %d, size(root(%d)) == %d", p, size[rootP - 1],
                q, size[rootQ - 1]));
        if (size[rootP - 1] < size[rootQ - 1]) {
            System.out.println(String.format("Set grid[%d][%d] (value=%d) = grid[%d][%d] (value %d)",
                    rowColRootP[0],
                    rowColRootP[1],
                    grid[rowColRootP[0]][rowColRootP[1]],
                    rowColRootQ[0],
                    rowColRootQ[1],
                    grid[rowColRootQ[0]][rowColRootQ[1]]));
            if (rowColRootP[0] == 0) {
                topIndexes.add(grid[rowColRootQ[0]][rowColRootQ[1]]);
            }
            if (rowColRootP[0] == grid.length - 1) {
                bottomIndexes.add(grid[rowColRootQ[0]][rowColRootQ[1]]);
            }
            grid[rowColRootP[0]][rowColRootP[1]] = grid[rowColRootQ[0]][rowColRootQ[1]];
            System.out.println(String.format("Size[%d] (%d) += size[%d] (%d)",
                    rootQ - 1,
                    size[rootQ - 1],
                    rootP - 1,
                    size[rootP - 1]));
            size[rootQ - 1] += size[rootP - 1];

        } else {
            System.out.println(String.format("Set grid[%d][%d] (value=%d) = grid[%d][%d] (value %d)",
                    rowColRootQ[0],
                    rowColRootQ[1],
                    grid[rowColRootQ[0]][rowColRootQ[1]],
                    rowColRootP[0],
                    rowColRootP[1],
                    grid[rowColRootP[0]][rowColRootP[1]]));
            if (rowColRootQ[0] == 0) {
                topIndexes.add(grid[rowColRootP[0]][rowColRootP[1]]);
            }
            if (rowColRootQ[0] == grid.length - 1) {
                bottomIndexes.add(grid[rowColRootP[0]][rowColRootP[1]]);
            }
            grid[rowColRootQ[0]][rowColRootQ[1]] = grid[rowColRootP[0]][rowColRootP[1]];
            System.out.println(String.format("Size[%d] (%d) += size[%d] (%d)",
                    rootP - 1,
                    size[rootP - 1],
                    rootQ - 1,
                    size[rootQ - 1]));
            size[rootP - 1] += size[rootQ - 1];
        }
    }

    private int[] getRowCol(int node) {
//        System.out.println(String.format("Get row columns of node=%d", node));

        int[] result = new int[2];
        int row = (node - 1) / grid.length;
        int col = Math.max(0, (node - 1) % grid.length);
        result[0] = row;
        result[1] = col;
//        System.out.println(String.format("row=%d, col=%d", row, col));
        return result;
    }

    private int findRoot(int node) {
        System.out.println(String.format("Find root of %d", node));
        int[] rowCol = getRowCol(node);
        int p = grid[rowCol[0]][rowCol[1]];
        while (p != getNodeNum(rowCol[0] + 1, rowCol[1] + 1, grid.length)) {
            rowCol = getRowCol(p);
            p = grid[rowCol[0]][rowCol[1]];
        }
        System.out.println(String.format("Root of %d is %d", node, p));
        return p;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row - 1][col - 1] != 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        System.out.println(String.format("isFull ? row=%d, col=%d", row, col));
        if (grid[row - 1][col - 1] == 0) {
            System.out.println("false");
            return false;
        }
        int node = getNodeNum(row, col, grid.length);
        int root = findRoot(node);
        return topIndexes.contains(root);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    private boolean connected(int p, int q) {
        System.out.println(String.format("connected ? p=%d, q=%d", p, q));
        if (p == 0 || q == 0) {
            System.out.println("false");
            return false;
        }
        boolean isConnected = findRoot(p) == findRoot(q);
        System.out.println(String.format("connected ? p=%d, q=%d result=%s", p, q, isConnected));
        return isConnected;
    }

    // does the system percolate?
    public boolean percolates() {
        boolean result = topIndexes
                .stream()
                .anyMatch(i -> bottomIndexes.contains(findRoot(i)));
        if (result) {
            System.out.println(String.format("Percolates! Opened = %d", openSites));
        }
        return result;
    }
}
