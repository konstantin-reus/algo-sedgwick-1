package com.reus.algorithms1.puzzle8;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private final Node finalNode;
    private static final Comparator<Node> minQueueComparator = (o1, o2) -> {
        int distance1 = o1.getNumberOfMovesToReach() + o1.getBoard().manhattan();
        int distance2 = o2.getNumberOfMovesToReach() + o2.getBoard().manhattan();
        if (distance1 < distance2) {
            return -1;
        } else if (distance1 == distance2) {
            return 0;
        } else {
            return 1;
        }
    };
    private boolean isSolveable = true;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Board twin = initial.twin();

        MinPQ<Node> queue = new MinPQ<>(minQueueComparator);
        MinPQ<Node> queueTwin = new MinPQ<>(minQueueComparator);

        Node initialNode = new Node(initial, 0, null);
        Node initialTwinNode = new Node(twin, 0, null);
        queue.insert(initialNode);
        queueTwin.insert(initialTwinNode);

        Node currentNode = queue.delMin();
        Node currentTwinNode = queueTwin.delMin();

        while (!currentNode.getBoard().isGoal()) {
            if (currentTwinNode.getBoard().isGoal()) {
                isSolveable = false;
                break;
            }
            currentNode = queueNeighborNodes(queue, currentNode);
            currentTwinNode = queueNeighborNodes(queueTwin, currentTwinNode);
        }
        finalNode = currentNode;
    }

    private static Node queueNeighborNodes(MinPQ<Node> queue, Node currentNode) {
        Iterable<Board> neighbors = currentNode.getBoard().neighbors();
        Board previousBoard = currentNode.getPreviousNode() == null
                ? null
                : currentNode.getPreviousNode().getBoard();
        for (Board b : neighbors) {
            if (previousBoard != null && previousBoard.equals(b)) {
                continue;
            } else {
                Node childNode = new Node(b, currentNode.getNumberOfMovesToReach() + 1, currentNode);
                queue.insert(childNode);
            }
        }
        return queue.delMin();
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tile = new int[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
        Board b = new Board(tile);
        Solver s = new Solver(b);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolveable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return finalNode.getNumberOfMovesToReach();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Node current = finalNode;
        List<Board> solutionBoards = new ArrayList<>(current.getNumberOfMovesToReach());
        for (int i = 0; i < finalNode.getNumberOfMovesToReach(); i++) {
            solutionBoards.add(0, current.getBoard());
            current = current.getPreviousNode();
        }
        return solutionBoards;
    }

    private static class Node {
        private final Board board;
        private final int numberOfMovesToReach;
        private final Node previousNode;

        public Node(Board board, int numberOfMovesToReach, Node previousNode) {
            this.board = board;
            this.numberOfMovesToReach = numberOfMovesToReach;
            this.previousNode = previousNode;
        }

        public Board getBoard() {
            return board;
        }

        public int getNumberOfMovesToReach() {
            return numberOfMovesToReach;
        }

        public Node getPreviousNode() {
            return previousNode;
        }
    }
}
