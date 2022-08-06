package org.oka.multithreadingconcurrency.task4;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool that block when it has not any items or it full
 */
@Slf4j
public class BlockingObjectPool {
    private final Queue<Object> commonStorage = new ArrayDeque<Object>();
    private final Object FULL = new Object();
    private final Object EMPTY = new Object();
    private final Object MONITOR = new Object();

    @Getter
    private int size = 0;
    @Getter
    private final AtomicInteger numOfGets = new AtomicInteger();
    @Getter
    private final AtomicInteger numOfTakes = new AtomicInteger();

    /**
     * Creates filled pool of passed size
     *
     * @param size of pool
     */
    public BlockingObjectPool(int size) {
        this.size = size;
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public Object get() {
        // If empty -> put current thread to sleep
        this.waitIfEmpty();
        // Get the last element of the storage
        Object object = this.getLast();
        // Increment the counter (testing purposes)
        numOfGets.getAndAdd(1);
        // Wake up producer threads, put on hold because the storage was full
        synchronized (FULL) {
            FULL.notifyAll();
        }

        return object;
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public void take(Object object) {
        // If full, wait
        waitIfFull();
        // Add element
        append(object);
        // Increment the counter (testing purposes)
        numOfTakes.getAndAdd(1);
        // Wake up consumer threads, put on hold because the storage was empty
        synchronized (EMPTY) {
            EMPTY.notifyAll();
        }
    }

    // Returns the last element of the underlying storage. Protected for multi-threaded environments
    private synchronized Object getLast() {
        return commonStorage.poll();
    }

    // Appends an object to the underlying storage. Protected for multi-threaded environments
    public synchronized void append(Object o) {
        commonStorage.add(o);
    }

    // If nothing is in the storage, the calling thread is put to 'wait()' until another thread wakes it up
    private void waitIfEmpty() {
        if (empty()) {
            synchronized (EMPTY) {
                try {
                    EMPTY.wait();
                } catch (InterruptedException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    // IF the storage is full, the calling thread is put to wait() until other thread wakes it up
    private void waitIfFull() {
        if (full()) {
            try {
                synchronized (FULL) {
                    FULL.wait();
                }
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }
    }

    // Thread safe method to check if the underlying storage is empty
    private synchronized boolean empty() {
        return commonStorage.isEmpty();
    }

    // Thread safe method to check if the underlying storage is full
    private synchronized boolean full() {
        return commonStorage.size() == size;
    }
}