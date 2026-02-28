package com.functional.programming.runnable.interf;

import com.functional.programming.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 *  - In modern Java (starting with Java 9), ExecutorService implements AutoCloseable. That means you can (and ideally should) use it
 *      inside a try‑with‑resources block, so it shuts down cleanly when the block exits.
 *  - Prefer try‑with‑resources for ExecutorService — it’s cleaner, safer, and ensures threads don’t leak.
 *
 *  Executor Framework: ExecutorService
 *      - We submit tasks (Runnable or Callable) to an executor, which manages a pool of threads internally.
 *      - Pros:
 *          - Thread pooling → reuse threads, avoid overhead of creating new ones.
 *          - Flexible → fixed pool, cached pool, scheduled tasks.
 *          - Scalable → handles large numbers of tasks efficiently.
 *          - Graceful shutdown with shutdown() or try‑with‑resources (Java 9+).
 *          - Supports Callable for tasks that return results via Future.
 *      - Cons:
 *          - Slightly more setup than Traditional way of creating threads like new Thread(task).start().
 *          - You need to understand pool sizing and shutdown semantics.
 *
 *  Note:
 *      - When you call executor.submit(runnable), the executor picks one available worker thread from its pool and runs that task.
 *      - The same task instance won’t be split across multiple threads unless you explicitly design it that way (e.g., by submitting multiple
 *          tasks or using parallel streams).
 */
public class ExecutorServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(ExecutorServiceDemo.class);

    public static void main(String[] args) {

        Runnable task = () -> IntStream.range(0, 5).forEach(i -> log.info("{}: {}", i, Util.faker().name().firstName()));

        // Create a fixed thread pool with 2 threads
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(task);
            executor.submit(task);
        } // executor.shutdown() is called automatically here
    }
}
