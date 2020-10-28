package com.reus.algorithms1.kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node head;
    private int size;

    public KdTree() {
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(0.5, 0.5));
        k.insert(new Point2D(0.6, 0.6));
        k.insert(new Point2D(0.3, 0.3));
        k.draw();
        k.nearest(new Point2D(0.01, 0.01));
        System.out.println(1);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D toInsert = new Point2D(p.x(), p.y());
        if (head == null) {
            head = new Node(toInsert, DirectionSplit.VERTICAL);
            size++;
            return;
        }
        Node cur = head;
        while (cur != null) {
            DirectionSplit d = cur.getDirectionSplit();
            if (d == DirectionSplit.VERTICAL) {
                if (toInsert.x() < cur.getPoint().x()) {
                    if (cur.getLeft() == null) {
                        cur.setLeft(new Node(toInsert, DirectionSplit.HORIZONTAL));
                        size++;
                        return;
                    }
                    cur = cur.getLeft();
                } else {
                    if (cur.getRight() == null) {
                        cur.setRight(new Node(toInsert, DirectionSplit.HORIZONTAL));
                        size++;
                        return;
                    }
                    cur = cur.getRight();
                }
            } else {
                if (toInsert.y() < cur.getPoint().y()) {
                    if (cur.getLeft() == null) {
                        cur.setLeft(new Node(toInsert, DirectionSplit.VERTICAL));
                        size++;
                        return;
                    }
                    cur = cur.getLeft();
                } else {
                    if (cur.getRight() == null) {
                        cur.setRight(new Node(toInsert, DirectionSplit.VERTICAL));
                        size++;
                        return;
                    }
                    cur = cur.getRight();
                }
            }
        }
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
                if (p.x() < cur.getPoint().x()) {
                    cur = cur.getLeft();
                } else {
                    cur = cur.getRight();
                }
            } else {
                if (p.y() < cur.getPoint().y()) {
                    cur = cur.getLeft();
                } else {
                    cur = cur.getRight();
                }
            }
        }
        return false;
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
                if (p.x() > rect.xmin() && cur.getLeft() != null) {
                    queue.enqueue(cur.getLeft());
                }
                if (p.x() < rect.xmax() && cur.getRight() != null) {
                    queue.enqueue(cur.getRight());
                }
            } else {
                if (p.y() > rect.ymin() && cur.getLeft() != null) {
                    queue.enqueue(cur.getLeft());
                }
                if (p.y() < rect.ymax() && cur.getRight() != null) {
                    queue.enqueue(cur.getRight());
                }
            }
        }
        return result;
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

    private Point2D nearest(Node current, Point2D target) {
        if (current == null) {
            return null;
        }
        double currentDistance = current.getPoint().distanceTo(target);
        if (current.getDirectionSplit() == DirectionSplit.VERTICAL) {
            Node towardsTarget = current.getPoint().x() > target.x()
                    ? current.getLeft()
                    : current.getRight();
            Point2D firstBranchNearest = nearest(towardsTarget, target);
            if (firstBranchNearest != null && firstBranchNearest.distanceTo(target) < currentDistance) {
                return firstBranchNearest;
            } else {
                Node oppositeTarget = towardsTarget == current.getRight()
                        ? current.getLeft()
                        : current.getRight();
                Point2D secondBranchNearest = nearest(oppositeTarget, target);
                if (secondBranchNearest != null &&
                        secondBranchNearest.distanceTo(target) < currentDistance) {
                    return secondBranchNearest;
                } else {
                    return current.getPoint();
                }
            }
        } else {
            Node towardsTarget = current.getPoint().y() > target.y()
                    ? current.getLeft()
                    : current.getRight();
            Point2D firstBranchNearest = nearest(towardsTarget, target);
            if (firstBranchNearest != null && firstBranchNearest.distanceTo(target) < currentDistance) {
                return firstBranchNearest;
            } else {
                Node oppositeTarget = towardsTarget == current.getRight()
                        ? current.getLeft()
                        : current.getRight();
                Point2D secondBranchNearest = nearest(oppositeTarget, target);
                if (secondBranchNearest != null && secondBranchNearest.distanceTo(target) < currentDistance) {
                    return secondBranchNearest;
                } else {
                    return current.getPoint();
                }
            }
        }
    }

    private enum DirectionSplit {
        VERTICAL, HORIZONTAL;
    }

    private static class Node {
        private final Point2D point;
        private final DirectionSplit directionSplit;
        private Node left;
        private Node right;

        public Node(Point2D point, DirectionSplit directionSplit) {
            this.point = point;
            this.directionSplit = directionSplit;
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
    }
}
