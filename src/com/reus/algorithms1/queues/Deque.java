package com.reus.algorithms1.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size = 0;

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> n = new Node<>(item);
        n.next = head;
        if (head != null) {
            head.prev = n;
        }
        if (tail == null) {
            tail = n;
        }
        head = n;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> n = new Node<>(item);
        n.prev = tail;
        if (tail != null) {
            tail.next = n;
        }
        tail = n;
        if (head == null) {
            head = n;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item i = head.value;
        if (tail == head) {
            tail = null;
        }
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        size--;
        return i;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item i = tail.value;
        if (head == tail) {
            head = null;
        }
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        }
        size--;
        return i;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node<Item> cur = head;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public Item next() {
                if (cur == null) {
                    throw new NoSuchElementException();
                }
                Item i = cur.value;
                cur = cur.next;
                return i;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class Node<Item> {
        private Node<Item> next;
        private Node<Item> prev;
        private final Item value;

        public Node(Item value) {
            this.value = value;
        }
    }
}
