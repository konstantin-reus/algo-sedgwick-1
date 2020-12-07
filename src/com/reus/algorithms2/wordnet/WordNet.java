package com.reus.algorithms2.wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 */
public class WordNet {
    private final Map<String, Set<Integer>> nounToSynsets = new HashMap<>();
    private final Map<Integer, String> synsets = new HashMap<>();
    private final SAP sap;
    private Digraph digraph;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        readSynsets(synsets);
        readHypernyms(hypernyms);
        checkForRootedDag();
        sap = new SAP(digraph);
    }

    private void checkForRootedDag() {
        DirectedCycle d = new DirectedCycle(digraph);
        if (d.hasCycle()) {
            throw new IllegalArgumentException();
        }
        int rootsFound = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                rootsFound++;
            }
        }
        if (rootsFound != 1) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet("/Users/kreus/Downloads/synsets.txt", "/Users/kreus/Downloads/hypernyms.txt");

        System.out.println(w.distance("worm", "bird"));
        System.out.println(w.sap("worm", "bird"));

        System.out.println(w.distance("municipality", "region"));
        System.out.println(w.sap("municipality", "region"));

        System.out.println(w.distance("white_marlin", "mileage"));
        System.out.println(w.sap("white_marlin", "mileage"));

        System.out.println(w.distance("Black_Plague", "black_marlin"));
        System.out.println(w.sap("Black_Plague", "black_marlin"));

        System.out.println(w.distance("American_water_spaniel", "histology"));
        System.out.println(w.sap("American_water_spaniel", "histology"));

        System.out.println(w.distance("Brown_Swiss", "barrel_roll"));
        System.out.println(w.sap("Brown_Swiss", "barrel_roll"));
    }

    private void readHypernyms(String hypernymsFilename) {
        In hypernumsInput = new In(hypernymsFilename);
        String[] lines = hypernumsInput.readAllLines();
        hypernumsInput.close();
        digraph = new Digraph(lines.length);
        for (String s : lines) {
            String[] splitted = s.split(",");
            if (splitted.length < 2) {
                continue;
            }
            int synsetNumber = Integer.parseInt(splitted[0]);
            for (int i = 1; i < splitted.length; i++) {
                digraph.addEdge(synsetNumber, Integer.parseInt(splitted[i]));
            }
        }
    }

    private void readSynsets(String synsetsFilename) {
        In synsetsInput = new In(synsetsFilename);
        while (synsetsInput.hasNextLine()) {
            String synsetString = synsetsInput.readLine();
            String[] splitted = synsetString.split(",");
            if (splitted.length < 2) {
                continue;
            }
            int synsetNum = Integer.parseInt(splitted[0]);
            String synset = splitted[1];
            synsets.put(synsetNum, synset);
            String[] splittedSynonyms = synset.split("\\s");
            Set<String> synonyms = new HashSet<>();
            for (String s : splittedSynonyms) {
                synonyms.add(s);
                nounToSynsets.computeIfAbsent(s, a -> new HashSet<>())
                        .add(synsetNum);
            }
        }
        synsetsInput.close();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsets.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToSynsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        int ancestor = sap.ancestor(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
        if (ancestor == -1) {
            return null;
        } else {
            return synsets.get(ancestor);
        }
    }

}
