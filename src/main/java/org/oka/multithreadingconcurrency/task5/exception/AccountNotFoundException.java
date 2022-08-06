package org.oka.multithreadingconcurrency.task5.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception to represent that the account is not in the system
 */
@Slf4j
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
