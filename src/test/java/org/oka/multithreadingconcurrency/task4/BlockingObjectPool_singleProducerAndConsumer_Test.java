package org.oka.multithreadingconcurrency.task4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockingObjectPool_singleProducerAndConsumer_Test {
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

    @ParameterizedTest
    @MethodSource
    public void shouldProduceAndConsumeMultipleTimes(int poolSize, int numOfActors) throws InterruptedException {
        // Given
        BlockingObjectPool blockingObjectPool = new BlockingObjectPool(poolSize);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfActors);

        // When
        for (int i = 0; i < numOfActors; i++) {
            executorService.submit(() -> blockingObjectPool.take(new Object()));
            executorService.submit(blockingObjectPool::get);
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Shutting down the executor service...");
        }

        // Then
        assertThat(blockingObjectPool.getNumOfGets().intValue()).isEqualTo(numOfActors);
        assertThat(blockingObjectPool.getNumOfTakes().intValue()).isEqualTo(numOfActors);
    }

    private static Stream<Arguments> shouldProduceAndConsumeMultipleTimes() {
        return Stream.of(
                // Size of the pool, num of actors (producers and consumers)
                Arguments.of(5, 2),
                Arguments.of(10, 15),
                Arguments.of(1, 20)
        );
    }
}
