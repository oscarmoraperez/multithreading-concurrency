package org.oka.multithreadingconcurrency.task5.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Represents an error because the currency of the account and the exchange rate are different
 */
@Slf4j
public class AccountCurrencyMismatch extends RuntimeException {
    public AccountCurrencyMismatch(String message) {
        super(message);
        log.error(message);
    }
}
