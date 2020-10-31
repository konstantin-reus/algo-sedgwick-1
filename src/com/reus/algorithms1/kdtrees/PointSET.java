package com.reus.algorithms1.kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 */
public class PointSET {
    private final TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public static void main(String[] args) {
        PointSET p = new PointSET();
        p.insert(new Point2D(0.5, 0.0));
        p.insert(new Point2D(1.0, 1.0));
        p.insert(new Point2D(0.25, 0.0));
        p.insert(new Point2D(0.5, 1.0));
        p.insert(new Point2D(0.25, 0.5));
        p.insert(new Point2D(0.0, 0.0));
        p.insert(new Point2D(1.0, 1.0));
        p.insert(new Point2D(1.0, 0.0));
        p.insert(new Point2D(0.5, 0.0));
        p.insert(new Point2D(0.75, 0.0));
        System.out.println(p.nearest(new Point2D(0.5, 0.75)));
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale();
        StdDraw.setYscale();
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> result = new ArrayList<>();
        if (isEmpty()) {
            return result;
        }
        for (Point2D point : points) {
            double x = point.x();
            double y = point.y();
            if (x >= rect.xmin() && x <= rect.xmax() &&
                    y >= rect.ymin() && y <= rect.ymax()) {
                result.add(point);
            }
        }
        return result;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        points.add(p);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            double curDistance = point.distanceSquaredTo(p);
            if (curDistance < minDistance) {
                minDistance = curDistance;
                nearest = point;
            }
        }
        return nearest;
    }
}
