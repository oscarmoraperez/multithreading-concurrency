package org.oka.multithreadingconcurrency.task5.dao;

import lombok.SneakyThrows;
import org.oka.multithreadingconcurrency.task5.exception.AccountCreationException;
import org.oka.multithreadingconcurrency.task5.exception.AccountNotFoundException;
import org.oka.multithreadingconcurrency.task5.exception.AccountReadingException;
import org.oka.multithreadingconcurrency.task5.exception.AccountUpdateException;
import org.oka.multithreadingconcurrency.task5.model.Account;
import org.oka.multithreadingconcurrency.task5.model.AccountBalance;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.oka.multithreadingconcurrency.task5.model.AccountBalance.DATE_AMOUNT_SEPARATOR;

/**
 * DAO to hide the persistence operations
 */
public class AccountDAO {

    /**
     * Persists an account in the disk (using files)
     *
     * @param account
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public Account persistAccount(final Account account) {
        File file = account.getFile();

        if (file.exists()) {
            throw new AccountCreationException("Cannot create Account: " + account + ". Already exists.");
        }
        if (!file.createNewFile()) {
            throw new AccountCreationException("Cannot create Account: " + account);
        }
        Files.write(file.toPath(), account.getBalance().stream().map(AccountBalance::toString).collect(toList()));

        return account;
    }

    /**
     * Retrieves an account from the disk file system
     *
     * @param account
     * @return
     * @throws IOException
     */
    public Account getAccount(final Account account) {
        File file = account.getFile();

        if (!file.exists()) {
            throw new AccountNotFoundException("Account " + account + " not found!");
        }
        List<String> balances = null;
        try {
            balances = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new AccountReadingException("It was not possible to read the balances of the account");
        }

        Account copy = new Account(account.getAccountId(), account.getCurrency());

        for (String balance : balances) {
            String[] balanceArray = balance.split(DATE_AMOUNT_SEPARATOR);
            AccountBalance accountBalance = new AccountBalance(LocalDateTime.parse(balanceArray[0]), new BigDecimal(balanceArray[1]));
            copy.getBalance().add(accountBalance);
        }

        return copy;
    }

    /**
     * Updates the balance (by adding a new entry) of the account
     *
     * @param account
     * @param accountBalance
     * @return
     */
    public Account updateBalance(final Account account, final AccountBalance accountBalance) {
        Account accountCopy = this.getAccount(account);
        File file = accountCopy.getFile();
        boolean deleted = file.delete();

        if (!deleted) {
            throw new AccountUpdateException("Account " + account + " cannot be updated");
        }

        accountCopy.getBalance().add(accountBalance);

        return this.persistAccount(accountCopy);
    }
}
