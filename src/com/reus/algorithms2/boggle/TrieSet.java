package com.reus.algorithms2.boggle;

import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

/**
 * The {@code TrieSET} class represents an ordered set of strings over
 * the extended ASCII alphabet.
 * It supports the usual <em>add</em>, <em>contains</em>, and <em>delete</em>
 * methods. It also provides character-based methods for finding the string
 * in the set that is the <em>longest prefix</em> of a given prefix,
 * finding all strings in the set that <em>start with</em> a given prefix,
 * and finding all strings in the set that <em>match</em> a given pattern.
 * <p>
 * This implementation uses a 256-way trie.
 * The <em>add</em>, <em>contains</em>, <em>delete</em>, and
 * <em>longest prefix</em> methods take time proportional to the length
 * of the key (in the worst case). Construction takes constant time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 * <i>Algorithms in Java, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class TrieSet implements Iterable<String> {
    private static final int R = 256;        // extended ASCII

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    /**
     * Initializes an empty set of strings.
     */
    public TrieSet() {
    }

    /**
     * Does the set contain the given key?
     *
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    private Node get(Node x, char c) {
        if (x == null) {
            return null;
        }
        return x.next[c];
    }

    /**
     * Adds the key to the set if it is not already present.
     *
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.hasChildren = true;
            x.next[c] = add(x.next[c], key, d + 1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     *
     * @return the number of strings in the set
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     *
     * @return {@code true} if the set is empty, and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all of the keys in the set, as an iterator.
     * To iterate over all of the keys in a set named {@code set}, use the
     * foreach notation: {@code for (Key key : set)}.
     *
     * @return an iterator to all of the keys in the set
     */
    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    public boolean hasKeysWithPrefix(StringBuilder prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        Node x = root;
        for (int i = 0; i < prefix.length(); i++) {
            x = get(x, prefix.charAt(i));
            if (x == null) {
                return false;
            }
        }
        if (x == null) {
            return false;
        }
        if (x.isString) {
            return true;
        } else {
            return x.hasChildren;
        }
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        private boolean hasChildren;
    }

}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
