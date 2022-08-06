package org.oka.multithreadingconcurrency.task5.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the balance of an account at specific time (amount and date).
 */
@RequiredArgsConstructor
@Getter
@Setter
public class AccountBalance {
    public final static String DATE_AMOUNT_SEPARATOR = " -- ";

    private final LocalDateTime dateTime;
    private final BigDecimal amount;

    /**
     * Represents the balance at specific point in time. For example: 2022-08-08 -- 60
     *
     * @return
     */
    public String toString() {
        return String.format("%s%s%s", dateTime.toString(), DATE_AMOUNT_SEPARATOR, amount);
    }
}
