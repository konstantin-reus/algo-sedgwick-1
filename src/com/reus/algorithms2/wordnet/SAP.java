package com.reus.algorithms2.wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SAP {
    private final Digraph digraph;
    private final Map<String, Integer> lengths = new HashMap<>();
    private final Map<String, Integer> ancestors = new HashMap<>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        digraph = new Digraph(G);
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(6);
        digraph.addEdge(1, 0);
        digraph.addEdge(5, 0);
        digraph.addEdge(1, 2);
        digraph.addEdge(2, 3);
        digraph.addEdge(3, 4);
        digraph.addEdge(4, 5);

        SAP s = new SAP(digraph);

        System.out.println(s.length(0, 1));
        System.out.println(s.ancestor(3, 0));
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        String key = getKey(v, w);
        if (lengths.containsKey(key)) {
            return lengths.get(key);
        }
        List<Integer> vList = new ArrayList<>();
        vList.add(v);
        List<Integer> wList = new ArrayList<>();
        wList.add(w);
        int result = length(vList, wList);
        lengths.put(key, result);
        return result;
    }

    private String getKey(int v, int w) {
        return String.format("%d -> %d", v, w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        String key = getKey(v, w);
        if (ancestors.containsKey(key)) {
            return ancestors.get(key);
        }
        ArrayList<Integer> v1 = new ArrayList<>();
        v1.add(v);
        ArrayList<Integer> w1 = new ArrayList<>();
        w1.add(w);
        int result = ancestor(v1, w1);
        ancestors.put(key, result);
        return result;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        checkForNulls(v, w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        String key = getKey(v, w);
        if (lengths.containsKey(key)) {
            return lengths.get(key);
        }
        BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (!bpV.hasPathTo(i) || !bpW.hasPathTo(i)) {
                continue;
            }
            int pathV = bpV.distTo(i);
            int pathW = bpW.distTo(i);
            if (pathV != -1 && pathW != -1 && pathV != Integer.MAX_VALUE && pathW != Integer.MAX_VALUE
                    && (pathV + pathW < min)) {
                min = pathV + pathW;
            }
        }
        if (min == Integer.MAX_VALUE) {
            min = -1;
        }
        lengths.put(key, min);
        return min;
    }

    private String getKey(Iterable<Integer> v, Iterable<Integer> w) {
        StringBuilder builder = new StringBuilder();
        if (v != null) {
            for (int from : v) {
                builder.append(from)
                        .append(',');
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
        } else {
            builder.append("null");
        }
        builder.append(" -> ");
        if (w != null) {
            for (int from : w) {
                builder.append(from)
                        .append(',');
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
        } else {
            builder.append("null");
        }

        return builder.toString();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        checkForNulls(v, w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        String key = getKey(v, w);
        if (ancestors.containsKey(key)) {
            return ancestors.get(key);
        }
        BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        int sap = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            int pathV = bpV.distTo(i);
            int pathW = bpW.distTo(i);
            if (pathV != -1 && pathW != -1 && pathV != Integer.MAX_VALUE && pathW != Integer.MAX_VALUE
                    && (pathV + pathW < min)) {
                sap = i;
                min = pathV + pathW;
            }
        }
        if (sap == Integer.MAX_VALUE) {
            sap = -1;
        }
        ancestors.put(key, sap);
        return sap;
    }

    private void checkForNulls(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer i : w) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
    }

}
