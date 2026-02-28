package com.functional.programming.runnable.interf;

import com.functional.programming.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 *  - Runnable i/f is a functional i/f that has a single abstract method run() that takes no args & returns nothing.
 *  - Runnable i/f is used to represent a task that can be executed in a separate thread concurrently with other tasks.
 *
 *   Traditional Way: new Thread(runnable).start() / Thread.ofPlatform().start()
 *      - We directly create a Thread object, pass it a Runnable & call start()
 *      - Pros:
 *          - Simple and straightforward for small tasks.
 *          - Good for quick demos or one‑off background jobs
 *      - Cons:
 *          - You manage thread lifecycle manually (start, stop, join).
 *          - No pooling — every task creates a new OS thread, which is expensive.
 *          - Hard to control concurrency, scheduling, or reuse.
 *          - Risk of resource leaks if you forget to manage threads properly.
 *
 */
public class RunnableDemo {

    private static final Logger log = LoggerFactory.getLogger(RunnableDemo.class);

    public static void main(String[] args) {

        log.info("********* Main thread started *********");
        Runnable task = () -> IntStream.range(0, 5).forEach(i -> log.info("{}: {}", i, Util.faker().name().firstName()));

        // Java 17 way to create thread using Constructor
        Thread thread = new Thread(task, "child-thread");
        //thread.start();

        // Java 21 way to create platform thread using Fluent Builder
        Thread.ofPlatform().name("Thread-A").start(task);
        Thread.ofPlatform().name("Thread-B").start(task);

        IntStream.range(0, 5).forEach(i -> log.info("{}: {}", i, Util.faker().name().firstName()));
        log.info("********* Main thread ended *********");
    }
}
