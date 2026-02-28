package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 *  Callable Interface
 *      - Callable is similar to Runnable, but it can return a result and throw exceptions.
 *      - Callable is used when you need to perform a task that returns a result.
 *      - Used with ExecutorService to run tasks asynchronously and retrieve results via Future.
 *      - submit() accepts a Callable and returns a Future.
 *      - future.get() blocks until the result is available.
 */
public class CallableDemo {

    private static final Logger log = LoggerFactory.getLogger(CallableDemo.class);

    public static void main(String[] args) {

        Callable<List<String>> task = () -> {
            log.info("Generating names...");
            return IntStream.range(0, 5).mapToObj(i -> {
                Util.sleepSeconds(1);
                var name = Util.faker().name().firstName();
                log.info("{} : {}", i,name);
                return name;
            }).toList();
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<List<String>> future = executor.submit(task);
            log.info("Generated Names: {}", future.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
