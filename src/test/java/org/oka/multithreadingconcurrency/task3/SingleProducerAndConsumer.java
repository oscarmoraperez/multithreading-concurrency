package org.oka.multithreadingconcurrency.task3;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;

public class SingleProducerAndConsumer {
    @SneakyThrows
    @Test
    public void shouldAcceptOneProducerAndConsumer() {
        // Given
        DataItemQueue queue = new DataItemQueue();
        Producer producer = new Producer(queue);
        Thread producerThread = new Thread(producer);
        Consumer consumer = new Consumer(queue);
        Thread consumerThread = new Thread(consumer);

        // When
        producerThread.start();
        consumerThread.start();
        Thread.sleep(5000);

        // Then
        producer.stop();
        consumer.stop();
    }

    @SneakyThrows
    @Test
    public void shouldAcceptMultipleProducerAndConsumer() {
        // Given
        DataItemQueue queue = new DataItemQueue();
        List<Thread> producerList = asList(new Thread(new Producer(queue)), new Thread(new Producer(queue)), new Thread(new Producer(queue)));
        List<Thread> consumerList = asList(new Thread(new Consumer(queue)), new Thread(new Consumer(queue)), new Thread(new Consumer(queue)));

        // When
        producerList.forEach(Thread::start);
        consumerList.forEach(Thread::start);
        Thread.sleep(5000);

        // Then
        producerList.forEach(Thread::stop);
        consumerList.forEach(Thread::stop);
    }
}
