package org.oka.multithreadingconcurrency.task5;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Constants {
    // Keeps the unique id of the next account
    public final static AtomicInteger ACCOUNT_NUM = new AtomicInteger(1);
    // Keeps the folder where the files (one per account) will be stored
    public static File STORAGE = new File("target" + File.separator + RandomStringUtils.randomAlphabetic(5));

    static {
        STORAGE.mkdirs();
    }
}
