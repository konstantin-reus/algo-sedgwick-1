package com.reus.algorithms1.queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int tail = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this(16);
    }

    private RandomizedQueue(int k) {
        items = (Item[]) new Object[k];
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return tail;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        checkIncreaseSize();
        items[tail++] = item;
    }

    private void checkIncreaseSize() {
        if (tail == items.length - 1) {
            Item[] newItems = (Item[]) new Object[items.length * 2];
            System.arraycopy(items, 0, newItems, 0, items.length);
            items = newItems;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(tail);
        Item toReturn = items[randomIndex];
        items[randomIndex] = items[tail - 1];
        items[tail - 1] = null;
        tail--;
        checkDecreaseSize();
        return toReturn;
    }

    private void checkDecreaseSize() {
        if (tail < items.length / 4) {
            Item[] newItems = (Item[]) new Object[items.length / 2];
            System.arraycopy(items, 0, newItems, 0, tail);
            items = newItems;
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(tail);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        int[] indices = new int[tail];
        for (int i = 0; i < tail; i++) {
            indices[i] = i;
        }
        StdRandom.shuffle(indices);

        return new Iterator<Item>() {
            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter <= indices.length - 1;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return items[indices[counter++]];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
