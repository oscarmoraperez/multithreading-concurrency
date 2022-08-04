package org.oka.multithreadingconcurrency.task3;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * Represents a producer in the consumer-producer landscape.
 */
public class Producer implements Runnable {
    private final DataItemQueue queue;
    private volatile boolean run;

    public Producer(DataItemQueue queue) {
        this.queue = queue;
        this.run = true;
    }

    @Override
    public void run() {
        produce();
    }

    private void produce() {
        while (run) {
            DataItem item = new DataItem(randomUUID(), randomAlphabetic(16));

            // If full, put the tread to sleep until awakened by other thread
            while (queue.isFull()) {
                try {
                    queue.waitOnFull();
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (!run) {
                break;
            }
            // produce/put item in the thread
            queue.addItem(item);
            // awake threads if necessary
            queue.notifyAllForEmpty();
        }
    }

    public void stop() {
        run = false;
        queue.notifyAllForFull();
    }
}
