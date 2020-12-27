package com.reus.algorithms2.boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.HashSet;
import java.util.Set;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php
 */
public class BoggleSolver {
    private final TrieSET dictionary = new TrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String word : dictionary) {
            this.dictionary.add(word);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        Set<String> result = new HashSet<>();
        for (int y = 0; y < board.rows(); y++) {
            for (int x = 0; x < board.cols(); x++) {
                dfs(board, y, x, result, new boolean[board.rows()][board.cols()], "");
            }
        }
        return result;
    }

    private void dfs(BoggleBoard board, int y, int x, Set<String> result, boolean[][] visited, String prefix) {
        if (y < 0 || x < 0 || y > board.rows() - 1 || x > board.cols() - 1) {
            return;
        }
        if (visited[y][x]) {
            return;
        }
        char letter = board.getLetter(y, x);
        String newPrefix;
        if (letter == 'Q') {
            newPrefix = prefix + "QU";
        } else {
            newPrefix = prefix + letter;
        }
        if (newPrefix.length() > 2 && dictionary.contains(newPrefix)) {
            result.add(newPrefix);
        }
        visited[y][x] = true;
        Iterable<String> strings = dictionary.keysWithPrefix(newPrefix);
        if (!strings.iterator().hasNext()) {
            return;
        }
        dfs(board, y - 1, x - 1, result, copyOf(visited), newPrefix);
        dfs(board, y - 1, x, result, copyOf(visited), newPrefix);
        dfs(board, y - 1, x + 1, result, copyOf(visited), newPrefix);
        dfs(board, y, x - 1, result, copyOf(visited), newPrefix);
        dfs(board, y, x + 1, result, copyOf(visited), newPrefix);
        dfs(board, y + 1, x - 1, result, copyOf(visited), newPrefix);
        dfs(board, y + 1, x, result, copyOf(visited), newPrefix);
        dfs(board, y + 1, x + 1, result, copyOf(visited), newPrefix);
    }

    private boolean[][] copyOf(boolean[][] source) {
        boolean[][] result = new boolean[source.length][source[0].length];
        for (int y = 0; y < source.length; y++) {
            System.arraycopy(source[y], 0, result[y], 0, source[y].length);
        }
        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word) || word.length() < 3) {
            return 0;
        }
        int length = word.length();
        if (length <= 4) {
            return 1;
        } else if (length == 5) {
            return 2;
        } else if (length == 6) {
            return 3;
        } else if (length == 7) {
            return 5;
        } else if (length >= 8) {
            return 11;
        } else {
            throw new IllegalStateException();
        }

    }
}
