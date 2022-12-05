# Multithreading and concurrency

This maven-based project contains  a set of exercises to train on concurrency and multithreading topics. 

### Task 1

All the related files are contained in the package: org.oka.multithreadingconcurrency.task1

SubTask 1 (SubTask_1.java)

- Main method with 2 threads accessing to a non-thread safe Map. When running the main method, a
  ConcurrentModificationException occurs

SubTask 2 (SubTask_2.java)

- Main method with 2 threads accessing to a thread safe Map ConcurrentHashMap. When running the main method, the
  execution is completed successfully.
- Interesting noticing that the use of ConcurrentHashMap fix the issue with the multiple concurrent threads accessing to
  the same data structure.

Subtask 3 (SubTask_3.java)

- Main method with 2 threads accessing to a custom (using synchronized keyword) thread-safe implemented Map (
  ThreadSafeMap.java in the same package).

The following comparison has been made:

| Implementation  | Java 6  | Java 8 | Java 11 | Java 14 |
|---|---|---|---|---|
| ConcurrentHashMap  | 1636 ms | 1322 ms. | 2964 ms. | 3125 ms. |  
| ThreadSafeMap (custom implementation) |  1560 ms. | 1087 ms.  | 1655 ms. | 1634 ms. |   

### Task 2

All the related files are contained in the package: org.oka.multithreadingconcurrency.task2

It consists in a class with main project where 3 Runnable inner classes are defined. Those 3 inner classes work on the
same data structure. They are thread - safe via the use of synchronize keyword.

### Task 3

All the related files are contained in the package: org.oka.multithreadingconcurrency.task3

Producer-Consumer landscape implementation with the following parts:

- Consumer.java
- Producer.java
- DataItemQueue.java

No concurrent classes from java. The implementation is thread safe by using monitors, waits and synchronize.

Tested multiple times (with multiple threads) in the test folder (same package)

### Task 4

All the related files are contained in the package: org.oka.multithreadingconcurrency.task4

Implementation of a custom thread-safe BlockingObjectPool, using monitor, synchronize.

Tested multiple times (with multiple threads) in the test folder (same package)

### Task 5

All the related files are contained in the package: org.oka.multithreadingconcurrency.task5

Thread-safe AccountService.java that manages accounts: create/addFunds/retrieveAccount/exchangeFunds.

Tested in a multithread environment See test/ folder.
