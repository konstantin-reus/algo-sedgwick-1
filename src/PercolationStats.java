import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double[] confidenceInterval;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        double[] ratios = new double[trials];
        for (int i = 0; i < trials; i++) {
            System.out.println("\n\n****    NEW TRIAL    ****");
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int randomColumn = StdRandom.uniform(1, n + 1);
                int randomRow = StdRandom.uniform(1, n + 1);
                p.open(randomRow, randomColumn);
                if (p.numberOfOpenSites() > 39999) {
                    System.out.println(1);
                }
            }
            double ratio = p.numberOfOpenSites() * 1.0 / (n * n);
            ratios[i] = ratio;
        }
        confidenceInterval = get95ConfidenceInterval(ratios, trials);
        mean = StdStats.mean(ratios);
        stddev = StdStats.stddev(ratios);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        System.out.println(String.format("\n\nmean = %f\nstddev=%f\n95 percent confidence interval = [%f, %f]",
                ps.mean(),
                ps.stddev(),
                ps.confidenceLo(),
                ps.confidenceHi()));

    }

    private static double[] get95ConfidenceInterval(double[] ratios, int t) {
        double[] result = new double[2];
        double mean = StdStats.var(ratios);
        result[0] = mean - (1.96 / Math.sqrt(t));
        result[1] = mean + (1.96 / Math.sqrt(t));
        return result;
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
        return confidenceInterval[0];
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceInterval[1];
    }
}
