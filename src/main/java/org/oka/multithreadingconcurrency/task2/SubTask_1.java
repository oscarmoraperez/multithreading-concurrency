package org.oka.multithreadingconcurrency.task2;

import java.util.ArrayList;
import java.util.List;

import static org.oka.multithreadingconcurrency.task2.Constants.ITERATIONS;

public class SubTask_1 {
    static List<Integer> listOfNumber = new ArrayList<Integer>();
    static final Object monitor = new Object();

    public static void main(String[] args) {
        SubTask_1 task1 = new SubTask_1();

        task1.go();
    }

    public void go() {
        NumInserter numInserter = new NumInserter();
        NumSummer numSummer = new NumSummer();
        NumSqrt numSqrt = new NumSqrt();

        numInserter.start();

        numSummer.start();

        numSqrt.start();
    }

    class NumInserter extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (monitor) {
                    listOfNumber.add(i);
                }
            }
        }
    }

    class NumSummer extends Thread {
        @Override
        public void run() {
            int sum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                int num = 0;
                synchronized (monitor) {
                    num = listOfNumber.get(i);
                }
                sum = sum + num;
                System.out.println("T2: Accumulated sum: " + sum);
            }
            System.out.println("T2: Final sum: " + sum);
        }
    }

    class NumSqrt extends Thread {
        @Override
        public void run() {
            int num = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (monitor) {
                    num = listOfNumber.get(i);
                }
                System.out.println("T3: Sqrt num: " + Math.sqrt(num));
            }
            System.out.println("T3: Final sqrt num: " + Math.sqrt(num));
        }
    }
}
