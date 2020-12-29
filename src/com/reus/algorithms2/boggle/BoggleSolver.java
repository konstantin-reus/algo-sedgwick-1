package com.reus.algorithms2.boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php
 */
public class BoggleSolver {
    private final TrieSet dictionary = new TrieSet();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String word : dictionary) {
            if (word.length() < 3) {
                continue;
            }
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
                dfs(board, y, x, result, new boolean[board.rows()][board.cols()], new StringBuilder());
            }
        }
        return result;
    }

    private void dfs(BoggleBoard board, int y, int x, Set<String> result, boolean[][] visited, StringBuilder prefix) {
        if (y < 0 || x < 0 || y > board.rows() - 1 || x > board.cols() - 1) {
            return;
        }
        if (visited[y][x]) {
            return;
        }
        if (!dictionary.hasKeysWithPrefix(prefix)) {
            return;
        }
        visited[y][x] = true;
        char letter = board.getLetter(y, x);
        boolean qAdded = false;
        if (letter == 'Q') {
            prefix.append("QU");
            qAdded = true;
        } else {
            prefix.append(letter);
        }
        if (!dictionary.hasKeysWithPrefix(prefix)) {
            prefix.deleteCharAt(prefix.length() - 1);
            if (qAdded) {
                prefix.deleteCharAt(prefix.length() - 1);
            }
            visited[y][x] = false;
            return;
        }
        if (prefix.length() > 2 && dictionary.contains(prefix.toString())) {
            result.add(prefix.toString());
        }
        dfs(board, y - 1, x - 1, result, visited, prefix);
        dfs(board, y - 1, x, result, visited, prefix);
        dfs(board, y - 1, x + 1, result, visited, prefix);
        dfs(board, y, x - 1, result, visited, prefix);
        dfs(board, y, x + 1, result, visited, prefix);
        dfs(board, y + 1, x - 1, result, visited, prefix);
        dfs(board, y + 1, x, result, visited, prefix);
        dfs(board, y + 1, x + 1, result, visited, prefix);
        prefix.deleteCharAt(prefix.length() - 1);
        if (qAdded) {
            prefix.deleteCharAt(prefix.length() - 1);
        }
        visited[y][x] = false;
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
