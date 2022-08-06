package org.oka.multithreadingconcurrency.task5;

import org.junit.jupiter.api.Test;
import org.oka.multithreadingconcurrency.task5.exception.AccountCurrencyMismatch;
import org.oka.multithreadingconcurrency.task5.exception.AccountWithoutEnoughFunds;
import org.oka.multithreadingconcurrency.task5.model.Account;
import org.oka.multithreadingconcurrency.task5.model.ExchangeRate;
import org.oka.multithreadingconcurrency.task5.service.AccountService;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Iterables.getLast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.oka.multithreadingconcurrency.task5.model.Currency.*;

public class AccountService_Test {
    AccountService accountService = new AccountService();

    @Test
    public void shouldCreateAccount() {
        // Given

        // When
        Account account = accountService.createAccount(CHF);

        // Then
        assertThat(account.getAccountId()).isNotZero();
        assertThat(account.getCurrency()).isEqualTo(CHF);
    }

    @Test
    public void shouldAddAmount() {
        // Given
        Account account = accountService.createAccount(CHF);

        // When
        Account updated = accountService.addFunds(account, new BigDecimal(55));

        // Then
        assertThat(getLast(updated.getBalance()).getAmount()).isEqualByComparingTo("55");
    }

    @Test
    public void shouldExchangeBetweenAccounts() {
        // Given
        Account originAccount = accountService.createAccount(CHF);
        accountService.addFunds(originAccount, new BigDecimal(55));
        Account destinationAccount = accountService.createAccount(USD);
        accountService.addFunds(destinationAccount, new BigDecimal(55));
        ExchangeRate rate = new ExchangeRate(CHF, USD, new BigDecimal("1.2"));

        // When
        accountService.exchange(originAccount, destinationAccount, new BigDecimal("40"), rate);

        // Then
        assertThat(getLast(accountService.getAccount(originAccount).getBalance()).getAmount()).isEqualByComparingTo("15");
        assertThat(getLast(accountService.getAccount(destinationAccount).getBalance()).getAmount()).isEqualByComparingTo("103");
    }

    @Test
    public void shouldThrowExceptionWhenNotEnoughFunds() {
        // Given
        Account originAccount = accountService.createAccount(CHF);
        accountService.addFunds(originAccount, new BigDecimal(55));
        Account destinationAccount = accountService.createAccount(USD);
        accountService.addFunds(destinationAccount, new BigDecimal(55));
        ExchangeRate rate = new ExchangeRate(CHF, USD, new BigDecimal("1.2"));

        // When..Then
        assertThrows(AccountWithoutEnoughFunds.class, () -> accountService.exchange(originAccount, destinationAccount, new BigDecimal("56"), rate));
    }

    @Test
    public void shouldThrowExceptionOriginAccountIsNotInTheExchangeCurrency() {
        // Given
        Account originAccount = accountService.createAccount(EUR);
        accountService.addFunds(originAccount, new BigDecimal(55));
        Account destinationAccount = accountService.createAccount(USD);
        accountService.addFunds(destinationAccount, new BigDecimal(55));
        ExchangeRate rate = new ExchangeRate(CHF, USD, new BigDecimal("1.2"));

        // When..Then
        assertThrows(AccountCurrencyMismatch.class, () -> accountService.exchange(originAccount, destinationAccount, new BigDecimal("50"), rate));
    }

    @Test
    public void shouldThrowExceptionDestinationAccountIsNotInTheExchangeCurrency() {
        // Given
        Account originAccount = accountService.createAccount(CHF);
        accountService.addFunds(originAccount, new BigDecimal(55));
        Account destinationAccount = accountService.createAccount(EUR);
        accountService.addFunds(destinationAccount, new BigDecimal(55));
        ExchangeRate rate = new ExchangeRate(CHF, USD, new BigDecimal("1.2"));

        // When..Then
        assertThrows(AccountCurrencyMismatch.class, () -> accountService.exchange(originAccount, destinationAccount, new BigDecimal("50"), rate));
    }

    @Test
    public void shouldExchangeWithMultipleThreads() throws InterruptedException {
        // Given
        Account originAccount = accountService.createAccount(CHF);
        accountService.addFunds(originAccount, new BigDecimal(55));
        Account destinationAccount = accountService.createAccount(USD);
        accountService.addFunds(destinationAccount, new BigDecimal(55));
        ExchangeRate rate = new ExchangeRate(CHF, USD, new BigDecimal("1.1"));
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // When
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> accountService.exchange(originAccount, destinationAccount, new BigDecimal(1), rate));
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Then
        assertThat(accountService.getAccount(originAccount).getBalance()).hasSize(12);
        assertThat(accountService.getAccount(destinationAccount).getBalance()).hasSize(12);
    }
}
