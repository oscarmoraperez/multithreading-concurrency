package org.oka.multithreadingconcurrency.task5.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static org.oka.multithreadingconcurrency.task5.Constants.STORAGE;

/**
 * Represents an account (accountId, currency and collection of balances)
 */
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    private final int accountId;
    private final Currency currency;
    Collection<AccountBalance> balance = new ArrayList<>();

    /**
     * Returns the file name where this account is persisted
     *
     * @return
     */
    public File getFile() {
        return new File(STORAGE + File.separator + String.format("%010d.%s.txt", accountId, currency));
    }
}
