package com.reus.algorithms1.collinear;

import java.util.Arrays;

public class BruteCollinearPoints {
    private static final int MIN_REPEATED_SLOTS = 3;
    private LineSegment[] segments = new LineSegment[0];
    private int counter = 0;

    /**
     * finds all line segments containing 4 points
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] clonedPoints = Arrays.copyOf(points, points.length);
        checkPoints(clonedPoints);
        segments = new LineSegment[clonedPoints.length * clonedPoints.length];
        for (int i = 0; i <= clonedPoints.length - 4; i++) {
            for (int j = i + 1; j <= clonedPoints.length - 3; j++) {
                for (int k = j + 1; k <= clonedPoints.length - 2; k++) {
                    for (int l = k + 1; l <= clonedPoints.length - 1; l++) {
                        if (clonedPoints[i] == null || clonedPoints[j] == null || clonedPoints[k] == null || clonedPoints[l] == null
                                || clonedPoints[i].equals(clonedPoints[j]) || clonedPoints[i].equals(clonedPoints[k]) || clonedPoints[i].equals(clonedPoints[l])
                                || clonedPoints[j].equals(clonedPoints[k]) || clonedPoints[j].equals(clonedPoints[l]) || clonedPoints[k].equals(clonedPoints[l])) {
                            throw new IllegalArgumentException();
                        }
                        Point[] tmp = new Point[]{clonedPoints[i], clonedPoints[j], clonedPoints[k], clonedPoints[l]};
                        Arrays.sort(tmp, Point::compareTo);
                        double slope1 = tmp[0].slopeTo(tmp[1]);
                        double slope2 = tmp[1].slopeTo(tmp[2]);
                        double slope3 = tmp[2].slopeTo(tmp[3]);
                        if (slope1 == slope2 && slope2 == slope3) {
                            segments[counter++] = new LineSegment(tmp[0], tmp[3]);
                        }
                    }
                }
            }
        }
    }

    private void checkPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        if (points.length == 0) {
            return;
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null
                    || points[i + 1] == null
                    || points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * the number of line segments
     *
     * @return
     */
    public int numberOfSegments() {
        return counter;
    }

    /**
     * the line segments
     *
     * @return
     */
    public LineSegment[] segments() {
        return Arrays.copyOfRange(segments, 0, counter);
    }
}
