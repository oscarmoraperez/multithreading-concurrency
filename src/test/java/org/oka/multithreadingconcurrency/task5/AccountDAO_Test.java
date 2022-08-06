package org.oka.multithreadingconcurrency.task5;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;
import org.oka.multithreadingconcurrency.task5.dao.AccountDAO;
import org.oka.multithreadingconcurrency.task5.model.Account;
import org.oka.multithreadingconcurrency.task5.model.AccountBalance;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.oka.multithreadingconcurrency.task5.model.Currency.USD;

public class AccountDAO_Test {
    AccountDAO accountDAO = new AccountDAO();

    @Test
    public void shouldCreateAccountFile() throws IOException {
        // Given
        Account account = new Account(new Random().nextInt(), USD);
        account.getBalance().add(new AccountBalance(LocalDateTime.now(), new BigDecimal(10)));

        // When
        accountDAO.persistAccount(account);

        // Then
        File fileAccount = account.getFile();
        assertThat(fileAccount).exists();
        assertThat(Files.readAllLines(fileAccount.toPath())).hasSize(1);
    }

    @Test
    public void shouldGetAccount() {
        // Given
        int id = new Random().nextInt();
        Account account = new Account(id, USD);
        account.getBalance().add(new AccountBalance(LocalDateTime.now().minusDays(1), new BigDecimal(10)));
        account.getBalance().add(new AccountBalance(LocalDateTime.now(), new BigDecimal(20)));
        accountDAO.persistAccount(account);

        // When
        Account accountRetrieved = accountDAO.getAccount(new Account(id, USD));

        // Then
        assertThat(accountRetrieved.getBalance()).hasSize(2);
    }

    @Test
    public void shouldUpdateAccountBalance() {
        // Given
        Account account = new Account(new Random().nextInt(), USD);
        account.getBalance().add(new AccountBalance(LocalDateTime.now().minusDays(1), new BigDecimal(10)));
        account.getBalance().add(new AccountBalance(LocalDateTime.now(), new BigDecimal(20)));
        accountDAO.persistAccount(account);
        LocalDateTime updateTime = LocalDateTime.now().plusDays(4);

        // When
        accountDAO.updateBalance(account, new AccountBalance(updateTime, new BigDecimal(55)));

        // Then
        Account accountFromSystem = accountDAO.getAccount(account);
        assertThat(Iterables.getLast(accountFromSystem.getBalance()).getDateTime()).isEqualTo(updateTime);
        assertThat(Iterables.getLast(accountFromSystem.getBalance()).getAmount()).isEqualByComparingTo("55");
    }
}
