import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] openSites;
    private int openCount;
    private final WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufFullness;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.n = n;
        openSites = new boolean[n * n];
        openCount = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufFullness = new WeightedQuickUnionUF(n * n + 1);
        virtualTop = n * n;
        virtualBottom = n * n + 1;
    }

    private void validateIndex(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private void connectToOpenNeighbor(int siteRow, int siteCol, int neighborRow, int neighborCol) {
        if (neighborRow >= 1 && neighborRow <= n && neighborCol >= 1 && neighborCol <= n && isOpen(neighborRow, neighborCol)) {
            int site = getIndex(siteRow, siteCol);
            int neighbor = getIndex(neighborRow, neighborCol);
            uf.union(site, neighbor);
            ufFullness.union(site, neighbor);
        }
    }

    public void open(int row, int col) {
        validateIndex(row, col);

        if (!isOpen(row, col)) {
            int index = getIndex(row, col);
            openSites[index] = true;
            openCount++;

            if (row == 1) {
                uf.union(index, virtualTop);
                ufFullness.union(index, virtualTop);
            }
            if (row == n) {
                uf.union(index, virtualBottom);
            }

            connectToOpenNeighbor(row, col, row - 1, col);
            connectToOpenNeighbor(row, col, row + 1, col);
            connectToOpenNeighbor(row, col, row, col - 1);
            connectToOpenNeighbor(row, col, row, col + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndex(row, col);
        return openSites[(row - 1) * n + (col - 1)];
    }

    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return ufFullness.find((row - 1) * n + (col - 1)) == ufFullness.find(virtualTop);
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        if (n == 1) {
            return isOpen(1, 1);
        }
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }
}