package org.oka.multithreadingconcurrency.task3;

/**
 * Represents a consumer in the consumer-producer landscape.
 */
public class Consumer implements Runnable {
    private final DataItemQueue queue;
    private volatile boolean run;

    public Consumer(DataItemQueue queue) {
        this.queue = queue;
        this.run = true;
    }

    @Override
    public void run() {
        consume();
    }

    public void consume() {
        while (run) {
            DataItem item;
            // If empty, put the thread to 'sleep' until some other (producer) thread wakes it again
            if (queue.isEmpty()) {
                try {
                    queue.waitOnEmpty();
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (!run) {
                break;
            }
            // Consume an item
            queue.removeItem();
            // wake up producer threads if necessary
            queue.notifyAllForFull();
            //System.out.println("Consumed Item: " + item.toString());
        }
    }

    public void stop() {
        run = false;
        queue.notifyAllForEmpty();
    }
}
