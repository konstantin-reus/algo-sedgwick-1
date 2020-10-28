package com.reus.algorithms1.kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() {
    }

    public static void main(String[] args) {
        PointSET p = new PointSET();
        p.insert(new Point2D(0., 0.));
        p.insert(new Point2D(0.1, 0.4));
        p.insert(new Point2D(0.4, 0.3));
        p.insert(new Point2D(0.6, 0.5));
        p.insert(new Point2D(0.8, 0.6));
        p.draw();


    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
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

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double curDistance = point.distanceTo(p);
            if (curDistance < minDistance) {
                nearest = point;
            }
        }
        return nearest;
    }
}
