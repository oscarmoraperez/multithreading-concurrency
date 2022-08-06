package org.oka.multithreadingconcurrency.task5.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Represents a problem during the creation of the account
 */
@Slf4j
public class AccountReadingException extends RuntimeException {
    public AccountReadingException(String message) {
        super(message);
        log.error(message);
    }
}
