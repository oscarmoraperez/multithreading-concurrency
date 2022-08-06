package org.oka.multithreadingconcurrency.task4;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockingObjectPool_singleProducerAndConsumer_IT {
    @Test
    public void shouldProduceAndConsume() {
        // Given
        BlockingObjectPool blockingObjectPool = new BlockingObjectPool(5);
        Object object = new Object();
        blockingObjectPool.take(object);

        // When
        Object object2 = blockingObjectPool.get();

        // Then
        assertThat(object2).isEqualTo(object);
    }

    @Test
    public void shouldProduceAndConsumeMultipleTimes2() throws InterruptedException {
        // Given
        BlockingObjectPool blockingObjectPool = new BlockingObjectPool(5);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // When
        executorService.submit(() -> blockingObjectPool.take(new Object()));
        executorService.submit(() -> blockingObjectPool.take(new Object()));
        executorService.submit(() -> blockingObjectPool.take(new Object()));
        executorService.submit(blockingObjectPool::get);
        executorService.submit(blockingObjectPool::get);
        executorService.submit(blockingObjectPool::get);
        executorService.shutdown();
        while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Shutting down the executor service...");
        }

        // Then
        assertThat(blockingObjectPool.getGets().intValue()).isEqualTo(3);
        assertThat(blockingObjectPool.getTakes().intValue()).isEqualTo(3);
    }
}
