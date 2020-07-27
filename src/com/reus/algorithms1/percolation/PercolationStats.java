package com.reus.algorithms1.percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] ratios;
    private final double trialsSqrt;
    private final double mean;
    private final double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t) {
        if (n < 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        ratios = new double[t];
        trialsSqrt = Math.sqrt(t);
        for (int i = 0; i < t; i++) {
//            System.out.println("\n\n****    NEW TRIAL    ****");
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int randomColumn = StdRandom.uniform(1, n + 1);
                int randomRow = StdRandom.uniform(1, n + 1);
                p.open(randomRow, randomColumn);
            }
            double ratio = p.numberOfOpenSites() * 1.0 / (n * n);
            ratios[i] = ratio;
        }
        this.mean = StdStats.mean(ratios);
        this.stddev = StdStats.stddev(ratios);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (1.96 * stddev / trialsSqrt);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (1.96 * stddev / trialsSqrt);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println(String.format("mean=%f\nstddev=%f\n95 percent confidence interval = [%f, %f]",
                ps.mean(),
                ps.stddev(),
                ps.confidenceLo(),
                ps.confidenceHi()));
    }
}
