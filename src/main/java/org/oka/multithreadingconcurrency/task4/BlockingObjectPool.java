package org.oka.multithreadingconcurrency.task4;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Pool that block when it has not any items or it full
 */
public class BlockingObjectPool {
    private final Queue<Object> commonStorage = new ArrayDeque<Object>();

    private final Object FULL = new Object();
    private final Object EMPTY = new Object();
    private int size = 0;

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
    public synchronized Object get() throws InterruptedException {
        if (commonStorage.isEmpty()) {
            synchronized (EMPTY) {
                EMPTY.wait();
            }
        }
        Object object = commonStorage.poll();
        FULL.notifyAll();
        return object;
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public synchronized void take(Object object) throws InterruptedException {
        if (commonStorage.size() == size) {
            FULL.wait();
        }
        commonStorage.add(object);
        synchronized (EMPTY) {
            EMPTY.notifyAll();
        }
    }
}