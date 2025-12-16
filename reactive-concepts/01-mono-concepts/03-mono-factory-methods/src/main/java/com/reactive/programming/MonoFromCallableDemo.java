package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

/**
 *  - Use Supplier when you want a simple, no‑argument value provider in functional style.
 *  - Use Callable when you want a task that can run asynchronously, return a result, and potentially throw exceptions.
 *
 *  - Use fromSupplier when you want a lazy constant or computation that won’t throw checked exceptions.
 *  - Use fromCallable when you need to wrap something that might throw exceptions (like file I/O, JDBC, etc.).
 *
 */
public class MonoFromCallableDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoFromCallableDemo.class);

    public static void main(String[] args) {

        var list = List.of(1, 2, 3, 4, 5);

        // This will give error: Unhandled exception: java.io.IOException. Also error will only show if publisher throws checked exception
        /*
        Mono.fromSupplier(() -> sum(list))
                .subscribe(DefaultSubscriber.create("sub1"));
        */
        // This will not complain & execute normally.
        Mono.fromCallable(() -> sum(list))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static int sum(List<Integer> list) throws IOException {
        log.info("Finding the sum of {}", list);
        return list.stream().reduce(0, Integer::sum);
    }
}
