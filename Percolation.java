/* *****************************************************************************
 * Name: Hasit Nanda
 * NetID:
 * Precept:
 *Description: This class models a percolation system using an n by n grid
 * of sites. Each site is either open or blocked. A full site is an open site
 * that can be connected to an open site in the top row via a chain of
 * neighboring open sites. The class determines whether the system percolates
 * depending on whether there is a full site in the bottom row.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // dimension of grid
    private int size;
    // 2D array to model grid
    private int[][] grid;
    // WeightedQuickUnionUF to track whether system percolates
    private WeightedQuickUnionUF uf;
    // WeightedQuickUnionUF to track whether sites are full
    private WeightedQuickUnionUF stopBackwash;
    // counter to track number of open sites
    private int counter;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        counter = 0;
        size = n;
        grid = new int[n][n];

        // 2 extra locations are created for virtual sites at top and bottom
        uf = new WeightedQuickUnionUF(size * size + 2);
        stopBackwash = new WeightedQuickUnionUF(size * size + 1);
    }

    // converts 2D grid location into 1D location in uf
    private int convert(int row, int col) {
        return row * size + col + 1;
    }

    // verifies that row and col are valid inputs based on grid dimensions
    private boolean validate(int row, int col) {
        if (row < 0 || row > size - 1 || col < 0 || col > size - 1) {
            return false;
        }
        else {
            return true;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        else if (grid[row][col] != 1) {
            grid[row][col] = 1;
            counter += 1;

            // Top row sites should be connected to virtual site at top
            if (row == 0) {
                uf.union(0, convert(row, col));
                stopBackwash.union(0, convert(row, col));
            }

            // Bottom row sites should be connected to virtual site at bottom
            if (row == size - 1) {
                uf.union(size * size + 1, convert(row, col));
            }

            // 4 conditions below will union current site to any of its open neighbours

            if (row > 0) {
                if (grid[row - 1][col] == 1) {
                    uf.union(convert(row, col), convert(row - 1, col));
                    stopBackwash.union(convert(row, col), convert(row - 1, col));
                }
            }

            if (row < size - 1) {
                if (grid[row + 1][col] == 1) {
                    uf.union(convert(row, col), convert(row + 1, col));
                    stopBackwash.union(convert(row, col), convert(row + 1, col));
                }
            }

            if (col > 0) {
                if (grid[row][col - 1] == 1) {
                    uf.union(convert(row, col), convert(row, col - 1));
                    stopBackwash.union(convert(row, col), convert(row, col - 1));
                }
            }

            if (col < size - 1) {
                if (grid[row][col + 1] == 1) {
                    uf.union(convert(row, col), convert(row, col + 1));
                    stopBackwash.union(convert(row, col), convert(row, col + 1));
                }
            }
        }
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        else {
            if (grid[row][col] == 1) {
                return true;
            }
            else {
                return false;
            }
        }


    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        else {
            if (stopBackwash.find(0) == stopBackwash.find(convert(row, col))) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {
        if (uf.find(0) == uf.find(size * size + 1)) {
            return true;
        }
        else {
            return false;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        test.open(1, 1);
        test.open(2, 1);

        // Should output true
        StdOut.println(test.isOpen(1, 1));

        // Should output false
        StdOut.println(test.isFull(2, 1));

        test.open(0, 1);

        // Should output true
        StdOut.println(test.isFull(2, 1));

        // Should output true
        StdOut.println(test.percolates());

        // Should output 3
        StdOut.println(test.numberOfOpenSites());

    }
}
