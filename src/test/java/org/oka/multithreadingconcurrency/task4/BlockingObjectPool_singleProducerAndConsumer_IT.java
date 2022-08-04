package org.oka.multithreadingconcurrency.task4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockingObjectPool_singleProducerAndConsumer_IT {
    @Test
    public void shouldProduceAndConsume() throws InterruptedException {
        // Given
        BlockingObjectPool blockingObjectPool = new BlockingObjectPool(5);
        Object object = new Object();
        blockingObjectPool.take(object);

        // When
        Object object2 = blockingObjectPool.get();

        // Then
        assertThat(object2).isEqualTo(object);
    }
}
