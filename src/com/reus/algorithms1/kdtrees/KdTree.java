package com.reus.algorithms1.kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 */
public class KdTree {
    private Node head;
    private int size;

    public KdTree() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();

        k.insert(new Point2D(0.875, 0.875));
        k.insert(new Point2D(0.125, 0.625));
        k.insert(new Point2D(0.25, 0.125));
        k.insert(new Point2D(0.75, 0.5));
        k.insert(new Point2D(1.0, 1.0));
        k.nearest(new Point2D(0, 0.75));

        k.insert(new Point2D(0.7, 0.2));
        k.insert(new Point2D(0.5, 0.4));
        k.insert(new Point2D(0.2, 0.3));
        k.insert(new Point2D(0.4, 0.7));
        k.insert(new Point2D(0.9, 0.6));
        System.out.println(k.nearest(new Point2D(0.719, 0.999)));
        k = new KdTree();
        k.insert(new Point2D(0.372, 0.497));
        k.insert(new Point2D(0.564, 0.413));
        k.insert(new Point2D(0.226, 0.577));
        k.insert(new Point2D(0.144, 0.179));
        k.insert(new Point2D(0.083, 0.51));
        k.insert(new Point2D(0.32, 0.708));
        k.insert(new Point2D(0.417, 0.362));
        k.insert(new Point2D(0.862, 0.825));
        k.insert(new Point2D(0.785, 0.725));
        k.insert(new Point2D(0.499, 0.208));
        System.out.println(k.nearest(new Point2D(0.868, 0.583)));
//        System.out.println(new Point2D(0.862, 0.825).distanceSquaredTo(new Point2D(0.868, 0.583)));
//        System.out.println(new Point2D(0.785, 0.725).distanceSquaredTo(new Point2D(0.868, 0.583)));
//        System.out.println(k.nearest(new Point2D(0.868, 0.583)));
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        Point2D toInsert = new Point2D(p.x(), p.y());
        if (head == null) {
            head = new Node(toInsert, DirectionSplit.VERTICAL, new RectHV(0, 0, 65536, 65536));
            size++;
            return;
        }
        Node cur = head;
        while (cur != null) {
            DirectionSplit d = cur.getDirectionSplit();
            if (d == DirectionSplit.VERTICAL) {
                if (toInsert.x() <= cur.getPoint().x()) {
                    if (cur.getLeft() == null) {
                        cur.setLeft(new Node(toInsert,
                                DirectionSplit.HORIZONTAL,
                                new RectHV(
                                        cur.getRectangle().xmin(),
                                        cur.getRectangle().ymin(),
                                        cur.getPoint().x(),
                                        cur.getRectangle().ymax())));
                        size++;
                        return;
                    }
                    cur = cur.getLeft();
                } else {
                    if (cur.getRight() == null) {
                        cur.setRight(new Node(toInsert,
                                DirectionSplit.HORIZONTAL,
                                new RectHV(
                                        cur.getPoint().x(),
                                        cur.getRectangle().ymin(),
                                        cur.getRectangle().xmax(),
                                        cur.getRectangle().ymax())));
                        size++;
                        return;
                    }
                    cur = cur.getRight();
                }
            } else {
                if (toInsert.y() <= cur.getPoint().y()) {
                    if (cur.getLeft() == null) {
                        cur.setLeft(new Node(toInsert, DirectionSplit.VERTICAL,
                                new RectHV(
                                        cur.getRectangle().xmin(),
                                        cur.getRectangle().ymin(),
                                        cur.getRectangle().xmax(),
                                        cur.getPoint().y())));
                        size++;
                        return;
                    }
                    cur = cur.getLeft();
                } else {
                    if (cur.getRight() == null) {
                        cur.setRight(new Node(toInsert, DirectionSplit.VERTICAL,
                                new RectHV(
                                        cur.getRectangle().xmin(),
                                        cur.getPoint().y(),
                                        cur.getRectangle().xmax(),
                                        cur.getRectangle().ymax())));
                        size++;
                        return;
                    }
                    cur = cur.getRight();
                }
            }
        }
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale();
        StdDraw.setYscale();
        StdDraw.setPenRadius(0.01);
        if (isEmpty()) {
            StdDraw.show();
            return;
        }
        Queue<Node> queue = new Queue<>();
        queue.enqueue(head);
        while (!queue.isEmpty()) {
            Node cur = queue.dequeue();
            StdDraw.point(cur.getPoint().x(), cur.getPoint().y());
            if (cur.getLeft() != null) {
                queue.enqueue(cur.getLeft());
            }
            if (cur.getRight() != null) {
                queue.enqueue(cur.getRight());
            }
        }
        StdDraw.show();
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node cur = head;
        while (cur != null) {
            if (cur.getPoint().equals(p)) {
                return true;
            }
            DirectionSplit d = cur.getDirectionSplit();
            if (d == DirectionSplit.VERTICAL) {
                if (cur.getPoint().x() >= p.x()) {
                    cur = cur.getLeft();
                } else {
                    cur = cur.getRight();
                }
            } else {
                if (cur.getPoint().y() >= p.y()) {
                    cur = cur.getLeft();
                } else {
                    cur = cur.getRight();
                }
            }
        }
        return false;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        if (contains(p)) {
            return p;
        }
        return nearest(head, p);

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> result = new ArrayList<>();
        if (isEmpty()) {
            return result;
        }
        Queue<Node> queue = new Queue<>();
        queue.enqueue(head);
        while (!queue.isEmpty()) {
            Node cur = queue.dequeue();
            Point2D p = cur.getPoint();
            if (rect.contains(p)) {
                result.add(p);
            }
            DirectionSplit d = cur.getDirectionSplit();
            if (d == DirectionSplit.VERTICAL) {
                if (p.x() >= rect.xmin() && cur.getLeft() != null) {
                    queue.enqueue(cur.getLeft());
                }
                if (p.x() < rect.xmax() && cur.getRight() != null) {
                    queue.enqueue(cur.getRight());
                }
            } else {
                if (p.y() >= rect.ymin() && cur.getLeft() != null) {
                    queue.enqueue(cur.getLeft());
                }
                if (p.y() < rect.ymax() && cur.getRight() != null) {
                    queue.enqueue(cur.getRight());
                }
            }
        }
        return result;
    }

    private enum DirectionSplit {
        VERTICAL, HORIZONTAL;
    }

    private Point2D nearest(Node current, Point2D target) {
        if (current == null) {
            return null;
        }

        Node towardsTarget;
        if (current.getDirectionSplit() == DirectionSplit.VERTICAL) {
            towardsTarget = current.getPoint().x() >= target.x()
                    ? current.getLeft()
                    : current.getRight();
        } else {
            towardsTarget = current.getPoint().y() >= target.y()
                    ? current.getLeft()
                    : current.getRight();
        }
        double currentDistance = current.getPoint().distanceSquaredTo(target);
        Point2D firstBranchNearest = nearest(towardsTarget, target);
        if (firstBranchNearest != null && firstBranchNearest.distanceSquaredTo(target) < currentDistance) {
            currentDistance = firstBranchNearest.distanceSquaredTo(target);
        }
        Node oppositeBranchDirection = towardsTarget == current.getRight()
                ? current.getLeft()
                : current.getRight();
        Point2D oppositeBranchNearest = null;
        if (oppositeBranchDirection != null && oppositeBranchDirection.getRectangle().distanceSquaredTo(target) < currentDistance) {
            oppositeBranchNearest = nearest(oppositeBranchDirection, target);
        }
        double towardShortest = firstBranchNearest == null
                ? Double.POSITIVE_INFINITY
                : firstBranchNearest.distanceSquaredTo(target);
        double oppositeShortest = oppositeBranchNearest == null
                ? Double.POSITIVE_INFINITY
                : oppositeBranchNearest.distanceSquaredTo(target);

        if (currentDistance < towardShortest && currentDistance < oppositeShortest) {
            return current.getPoint();
        } else if (towardShortest < currentDistance && towardShortest < oppositeShortest) {
            return firstBranchNearest;
        } else if (oppositeShortest < towardShortest && oppositeShortest < currentDistance) {
            return oppositeBranchNearest;
        } else if (towardShortest < currentDistance) {
            return firstBranchNearest;
        } else if (oppositeShortest < towardShortest) {
            return oppositeBranchNearest;
        } else if (currentDistance == towardShortest) {
            return firstBranchNearest;
        } else {
            throw new IllegalStateException("Cur = " + currentDistance + ", opp = " + oppositeShortest + ", towards = " + towardShortest);
        }
    }

    private static class Node {
        private final Point2D point;
        private final DirectionSplit directionSplit;
        private final RectHV rectangle;
        private Node left;
        private Node right;

        public Node(Point2D point, DirectionSplit directionSplit, RectHV rectangle) {
            this.point = point;
            this.directionSplit = directionSplit;
            this.rectangle = rectangle;
        }

        public Point2D getPoint() {
            return point;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public DirectionSplit getDirectionSplit() {
            return directionSplit;
        }

        public RectHV getRectangle() {
            return rectangle;
        }

    }
}
