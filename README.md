# Java Mentoring Program: multithreading and concurrency

This maven-based project contains the exercises defined in the 'multithreading and concurrency ' module of Java
Mentoring program.

### Task 1
All the related files are contained in the package: org.oka.multithreadingconcurrency.task1

SubTask 1 (SubTask_1.java)

- Main method with 2 threads accessing to a non-thread safe Map. When running the main method, a
  ConcurrentModificationException occurs

SubTask 2 (SubTask_2.java)

- Main method with 2 threads accessing to a thread safe Map ConcurrentHashMap. When running the main method, the
  execution is completed successfully.
- Interesting noticing that the use of ConcurrentHashMap fix the issue with the multiple concurrent threads accessing to the same data structure. 

Subtask 3 (SubTask_3.java)

- Main method with 2 threads accessing to a custom (using synchronized keyword) thread-safe implemented Map (
  ThreadSafeMap.java in the same package).

The following comparison has been made:

| Implementation  | Java 6  | Java 8 | Java 11 | Java 14 |
|---|---|---|---|---|
| ConcurrentHashMap  | 1636 ms | 1322 ms. | 2964 ms. | 3125 ms. |  
| ThreadSafeMap (custom implementation) |  1560 ms. | 1087 ms.  | 1655 ms. | 1634 ms. |   
