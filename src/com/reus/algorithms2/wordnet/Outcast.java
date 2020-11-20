package com.reus.algorithms2.wordnet;

public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public static void main(String[] args) {
        WordNet w = new WordNet("/Users/kreus/Downloads/synsets.txt", "/Users/kreus/Downloads/hypernyms.txt");
        Outcast o = new Outcast(w);
        System.out.println(o.outcast(new String[]{"horse", "zebra", "cat", "bear", "table"}));
        System.out.println(o.outcast(new String[]{"water", "soda", "bed", "orange_juice", "milk", "apple_juice", "tea", "coffee"}));
        System.out.println(o.outcast(new String[]{"apple", "pear", "peach", "banana", "lime", "lemon", "blueberry", "strawberry", "mango", "watermelon", "potato"}));
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int outcasted = 0;
        int curMaxDistance = 0;
        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (int j = 0; j < nouns.length; j++) {
                distance += wordNet.distance(nouns[i], nouns[j]);
            }
            if (distance > curMaxDistance) {
                curMaxDistance = distance;
                outcasted = i;
            }
        }
        return nouns[outcasted];
    }
}
