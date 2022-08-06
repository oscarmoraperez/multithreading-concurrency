package org.oka.multithreadingconcurrency.task5.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Represents a problem during the creation of the account
 */
@Slf4j
public class AccountCreationException extends RuntimeException {
    public AccountCreationException(String message) {
        super(message);
        log.error(message);
    }
}
