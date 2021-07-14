/* *****************************************************************************
 *  Name:    Hasit Nanda
 *  NetID:
 *  Precept:
 *
 *  Description: This class runs a Monte Carlo Simulation and outputs the
 * relevant data. The grid dimensions and number of trials are inputted and
 * Sites are opened at random until the system percolates. The mean, stdDev,
 * lowConfidence, highConfidence, and elapsed Time are outputed. *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    // stores proportion of sites opened for system to percolate for each trial
    private double[] thresholds;

    // stores elapsed time
    private double time;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        thresholds = new double[trials];
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < trials; i++) {
            Percolation newPercolation = new Percolation(n);

            while (!newPercolation.percolates()) {
                int siteRow = StdRandom.uniform(n);
                int siteCol = StdRandom.uniform(n);
                newPercolation.open(siteRow, siteCol);
            }

            double gridSize = n * n;
            thresholds[i] = newPercolation.numberOfOpenSites() / gridSize;
        }
        time = stopwatch.elapsedTime();
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        // This is the corresponding z-statistic for a 95% confidence level test
        double Z_STATISTIC = 1.96;
        return mean() - Z_STATISTIC * (stddev() / Math.sqrt(thresholds.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        // This is the corresponding z-statistic for a 95% confidence level test
        double Z_STATISTIC = 1.96;
        return mean() + Z_STATISTIC * (stddev() / Math.sqrt(thresholds.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats test = new PercolationStats(n, trials);
        StdOut.println(String.format("%20s", "Mean: ") +
                               String.format("%.6f", test.mean()));
        StdOut.println(String.format("%20s", "StdDev: ") +
                               String.format("%.6f", test.stddev()));
        StdOut.println(String.format("%20s", "ConfidenceLow: ") + String
                .format("%.6f", test.confidenceLow()));
        StdOut.println(String.format("%20s", "ConfidenceHigh: ") + String
                .format("%.6f", test.confidenceHigh()));
        StdOut.println(String.format("%20s", "Elapsed Time: ") +
                               String.format("%.3f", test.time));

    }

}
