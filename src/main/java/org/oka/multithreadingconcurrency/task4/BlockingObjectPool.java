package org.oka.multithreadingconcurrency.task4;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool that block when it has not any items or it full
 */
public class BlockingObjectPool {
    private final Queue<Object> commonStorage = new ArrayDeque<Object>();

    private final Object FULL = new Object();
    private final Object EMPTY = new Object();
    private final Object MONITOR = new Object();
    @Getter
    private int size = 0;
    @Getter
    private final AtomicInteger gets = new AtomicInteger();
    @Getter
    private final AtomicInteger takes = new AtomicInteger();

    /**
     * Creates filled pool of passed size
     *
     * @param size of pool
     */
    public BlockingObjectPool(int size) {
        this.size = size;
    }

    /**
     * Size getter
     *
     * @return
     */
    private int getSize() {
        return this.size;
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public Object get() {
        if (empty()) {
            synchronized (EMPTY) {
                try {
                    EMPTY.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Object object;
        synchronized (MONITOR) {
            object = commonStorage.poll();
        }
        gets.getAndAdd(1);
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
        if (full()) {
            try {
                synchronized (FULL) {
                    FULL.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (MONITOR) {
            commonStorage.add(object);
        }
        takes.getAndAdd(1);
        synchronized (EMPTY) {
            EMPTY.notifyAll();
        }
    }

    public synchronized boolean empty() {
        return commonStorage.isEmpty();
    }

    public synchronized boolean full() {
        return commonStorage.size() == this.size;
    }
}