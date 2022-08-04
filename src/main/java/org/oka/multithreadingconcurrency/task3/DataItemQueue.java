package org.oka.multithreadingconcurrency.task3;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Custom thread-safe queue to represent the backbone of producer-consumer schema.
 */
public class DataItemQueue {
    private final Queue<DataItem> commonStorage = new ArrayDeque<DataItem>();
    private final static int MAX_SIZE = 5000;

    private final Object FULL = new Object();
    private final Object EMPTY = new Object();
    private final Object MONITOR = new Object();

    /**
     * Utility to know if the underlying storage is full
     *
     * @return
     */
    public boolean isFull() {
        return commonStorage.size() == MAX_SIZE;
    }

    /**
     * Utility to know if the underlying storage is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return commonStorage.isEmpty();
    }

    /**
     * Puts the current producer thread on 'wait'. It will be awakened with the notifyAllForFull call.
     *
     * @throws InterruptedException
     */
    public void waitOnFull() throws InterruptedException {
        synchronized (FULL) {
            FULL.wait();
        }
    }

    /**
     * Notifies / awakes the producer threads put on sleep because the storage was full.
     */
    public void notifyAllForFull() {
        synchronized (FULL) {
            FULL.notifyAll();
        }
    }

    /**
     * Puts the current consumer thread on 'wait'. It will be awakened with the notifyAllForEmpty call.
     *
     * @throws InterruptedException
     */
    public void waitOnEmpty() throws InterruptedException {
        synchronized (EMPTY) {
            EMPTY.wait();
        }
    }

    /**
     * Notifies / awakes the threads put on sleep because the storage was empty.
     */
    public void notifyAllForEmpty() {
        synchronized (EMPTY) {
            EMPTY.notify();
        }
    }

    /**
     * Thread-safe method to add items to the underlying storage shared between producer and consumers.
     *
     * @param item
     */
    public void addItem(DataItem item) {
        synchronized (MONITOR) {
            commonStorage.add(item);
        }
    }

    /**
     * Thread-safe method to remove the last item of the underlying storage shared between producer and consumers.
     */
    public DataItem removeItem() {
        synchronized (MONITOR) {
            return commonStorage.poll();
        }
    }
}
