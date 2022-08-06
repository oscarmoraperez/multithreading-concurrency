package org.oka.multithreadingconcurrency.task5.service;

import com.google.common.collect.Iterables;
import org.oka.multithreadingconcurrency.task5.dao.AccountDAO;
import org.oka.multithreadingconcurrency.task5.exception.AccountCurrencyMismatch;
import org.oka.multithreadingconcurrency.task5.exception.AccountWithoutEnoughFunds;
import org.oka.multithreadingconcurrency.task5.model.Account;
import org.oka.multithreadingconcurrency.task5.model.AccountBalance;
import org.oka.multithreadingconcurrency.task5.model.Currency;
import org.oka.multithreadingconcurrency.task5.model.ExchangeRate;

import java.math.BigDecimal;

import static java.time.LocalDateTime.now;
import static org.oka.multithreadingconcurrency.task5.Constants.ACCOUNT_NUM;

/**
 * Service layer that provides a set of methods to manage accounts (create, get, addFunds, exchange)
 */
public class AccountService {
    // DAO to have access to the persisted data
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Creates and persists an account with a specific currency with balance zero.
     * No need to be thread safe since the numerical id generation is based on AtomicInteger.
     *
     * @param currency
     * @return
     */
    public Account createAccount(final Currency currency) {
        Account account = new Account(ACCOUNT_NUM.getAndAdd(1), currency);
        account.getBalance().add(new AccountBalance(now(), BigDecimal.ZERO));

        return accountDAO.persistAccount(account);
    }

    /**
     * Thread-safe method to add funds to an account.
     *
     * @param account
     * @param amount
     * @return
     */
    public synchronized Account addFunds(final Account account, final BigDecimal amount) {
        return accountDAO.updateBalance(account, new AccountBalance(now(), amount));
    }

    /**
     * Retrieves an account from the persisted accounts.
     *
     * @param account
     * @return
     */
    public Account getAccount(final Account account) {
        return accountDAO.getAccount(account);
    }

    /**
     * Exchanges funds between two accounts at a specific exchange rate
     *
     * @param originAccount
     * @param destinationAccount
     * @param amount
     * @param exchangeRate
     */
    public synchronized void exchange(final Account originAccount, final Account destinationAccount, final BigDecimal amount, final ExchangeRate exchangeRate) {
        Account originAccountFromSystem = accountDAO.getAccount(originAccount);
        Account destinationAccountFromSystem = accountDAO.getAccount(destinationAccount);

        if (!originAccountFromSystem.getCurrency().equals(exchangeRate.getOriginCurrency())) {
            throw new AccountCurrencyMismatch("Mismatch in the currency of the origin account and exchange rate. Account: " + originAccount + ". Exchange: " + exchangeRate.getOriginCurrency());
        }
        if (!destinationAccountFromSystem.getCurrency().equals(exchangeRate.getDestinationCurrency())) {
            throw new AccountCurrencyMismatch("Mismatch in the currency of the destination account and exchange rate. Account: " + destinationAccount + ". Exchange: " + exchangeRate.getDestinationCurrency());
        }
        AccountBalance lastBalanceOfOrigin = Iterables.getLast(originAccountFromSystem.getBalance());
        if (lastBalanceOfOrigin.getAmount().compareTo(amount) < 0) {
            throw new AccountWithoutEnoughFunds("Account " + originAccountFromSystem + " has not enough funds");
        }

        AccountBalance newBalanceOrigin = new AccountBalance(now(), lastBalanceOfOrigin.getAmount().subtract(amount));
        AccountBalance newBalanceDestination = new AccountBalance(now(), lastBalanceOfOrigin.getAmount().add(amount.multiply(exchangeRate.getRate())));

        accountDAO.updateBalance(originAccount, newBalanceOrigin);
        accountDAO.updateBalance(destinationAccount, newBalanceDestination);
    }
}
