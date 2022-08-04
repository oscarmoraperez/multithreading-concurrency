package org.oka.multithreadingconcurrency.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.oka.multithreadingconcurrency.task1.Constants.ITERATIONS;

public class SubTask_1 {
    Map<Integer, Integer> MAP_OF_NUMBER = new HashMap();

    public static void main(String[] args) {
        SubTask_1 task1 = new SubTask_1();

        task1.go();
    }

    public void go() {
        // Two threads are gonna interact with the same non-thread safe map
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
                MAP_OF_NUMBER.put(random.nextInt(), random.nextInt());
            }
        }
    }

    class NumSummer implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            for (Map.Entry<Integer, Integer> entry : MAP_OF_NUMBER.entrySet()) {
                MAP_OF_NUMBER.put(entry.getKey() + 10, random.nextInt());
            }
        }
    }
}
