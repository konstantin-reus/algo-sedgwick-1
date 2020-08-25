package com.reus.algorithms1.queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int current = 1;
        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            if (current <= k) {
                queue.enqueue(input);
            } else {
                double chance = k * 1.0 / current;
                double random = StdRandom.uniform();
                if (random < chance) {
                    if (!queue.isEmpty()) {
                        queue.dequeue();
                    }
                    queue.enqueue(input);
                }
            }
            current++;
        }
        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
