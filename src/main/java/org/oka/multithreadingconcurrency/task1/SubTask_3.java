package org.oka.multithreadingconcurrency.task1;

import java.util.Map;
import java.util.Random;

import static org.oka.multithreadingconcurrency.task1.Constants.ITERATIONS;

public class SubTask_3 {
    static ThreadSafeMap<Integer, Integer> MY_THREAD_SAFE_MAP = new ThreadSafeMap();

    public static void main(String[] args) {
        SubTask_3 task3 = new SubTask_3();

        task3.go();
    }

    public void go() {
        // Two threads are gonna interact with my custom thread safe map
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
                MY_THREAD_SAFE_MAP.put(random.nextInt(), random.nextInt());
            }
        }
    }

    class NumSummer implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            for (Map.Entry<Integer, Integer> entry : MY_THREAD_SAFE_MAP.entrySet()) {
                MY_THREAD_SAFE_MAP.put(entry.getKey() + 10, random.nextInt());
            }
        }
    }
}
