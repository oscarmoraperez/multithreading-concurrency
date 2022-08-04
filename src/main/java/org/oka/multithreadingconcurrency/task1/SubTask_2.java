package org.oka.multithreadingconcurrency.task1;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.oka.multithreadingconcurrency.task1.Constants.ITERATIONS;

public class SubTask_2 {
    static ConcurrentHashMap<Integer, Integer> SYNC_MAP_OF_NUMBER = new ConcurrentHashMap();

    public static void main(String[] args) {
        SubTask_2 task_2 = new SubTask_2();

        task_2.go();
    }

    public void go() {
        // Two threads are gonna interact with the same concurrent-thread safe map
        NumAdder numAdder = new NumAdder();
        NumSummer numSummer = new NumSummer();

        long tick = System.currentTimeMillis();

        numAdder.run();
        numSummer.run();

        long tack = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (tack - tick) + " ms.");
    }

    class NumAdder implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                SYNC_MAP_OF_NUMBER.put(random.nextInt(), random.nextInt());
            }
        }
    }

    class NumSummer implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            for (Map.Entry<Integer, Integer> entry : SYNC_MAP_OF_NUMBER.entrySet()) {
                SYNC_MAP_OF_NUMBER.put(entry.getKey() + 10, random.nextInt());
            }
        }
    }
}
