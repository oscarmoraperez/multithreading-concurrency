package org.oka.multithreadingconcurrency.task5.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents an Exchange rate.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class ExchangeRate {
    private final Currency originCurrency;
    private final Currency destinationCurrency;
    private final BigDecimal rate;
}
