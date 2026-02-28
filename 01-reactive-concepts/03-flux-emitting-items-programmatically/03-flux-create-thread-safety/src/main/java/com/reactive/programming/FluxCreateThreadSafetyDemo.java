package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

/**
 *  - FluxSink is thread-safe.
 *  - Example: Let's add items to ArrayList (by default not thread-safe) directly & next via Flux
 *      - Each time when we execute threadUnsafeExample(), we get different size of list as it's not thread safe.
 *      - Each time when we execute threadSafeExample(), we get same size of list as we're adding items via FluxSink
 *  - Explanation:
 *      - ArrayList is not thread-safe so when we try to add items to an ArrayList via multiple threads, it's not going to work
 *      - However in threadSafeExample(), we did the same thing i.e., we're getting items from multiple threads
 *          & adding them to ArrayList but not directly. Instead, all the threads will give the data via FluxSink.
 *          - Becoz FluxSink is thread-safe & it is shared with all the threads instead of list directly,
 *              it was able to transfer all the items to the list/subscriber sequentially one by one safely.
 */
public class FluxCreateThreadSafetyDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxCreateThreadSafetyDemo.class);

    public static void main(String[] args) {

        threadUnsafeExample();

        log.info("------------------------------------------");

        threadSafeExample();

    }

    // ArrayList is not thread safe
    private static void threadUnsafeExample() {
        log.info("Adding items to ArrayList...");
        var list = new ArrayList<String>();
        Runnable runnable = () -> {
            for (int i = 0; i < 10000; i++) {
                list.add(Util.faker().country().name());
            }
        };

        // Here we're creating 10 threads. Each thread adding 10,000 items to the list i.e., in total 1,00,000 items
        for (int i = 0; i < 10; i++) {
            // if Java 21 then use Thread.ofPlatform().start(runnable);
            new Thread(runnable).start();
        }

        Util.sleepSeconds(3);

        log.info("Added! Thread-unsafe List size: {}", list.size());
    }

    private static void threadSafeExample() {
        log.info("Adding items to ArrayList via Flux...");
        var list = new ArrayList<String>();
        var generator = new NameGenerator();
        var flux = Flux.create(generator);
        flux.subscribe(name -> list.add(name));

        Runnable runnable = () -> {
            for(int i = 0; i < 10000; i++) {
                generator.generate();
            }
        };

        // Here we're creating 10 threads. Each thread adding 10,000 items to the list i.e., in total 1,00,000 items
        for (int i = 0; i < 10; i++) {
            // if Java 21 then use Thread.ofPlatform().start(runnable);
            new Thread(runnable).start();
        }

        Util.sleepSeconds(3);

        log.info("Added! List size via fluxing: {}", list.size());
    }
}
