package org.oka.multithreadingconcurrency.task3;

import lombok.Data;

import java.util.UUID;

/**
 * Class that holds a message in the consumer - producer schema.
 * Only two fields used: random id, and text data in a string field.
 */
@Data
public class DataItem {
    private final UUID id;
    private final String data;
}
