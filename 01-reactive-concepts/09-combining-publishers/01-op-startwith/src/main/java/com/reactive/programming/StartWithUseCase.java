package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Use case:
 *      - Sometimes in real time, we will be doing a lot of computation & we want to store it in the cache
 *          so that each subscriber can read it from cache instead of doing the heavy computation every single time.
 *      - In these cases, startWith operator could be a good choice.
 *
 *  Scenario:
 *      - Generate the names for each subscriber & this operation: generating name is a time-consuming operation.
 *
 *  Case 1: without__startWith()
 *      - Each subscriber will generate the names independently.
 *      - Probably name generated for 1 subscriber could be same as another subscribers.
 *
 *  Case 2: with__startWith()
 *      - We can cache the names in the list.
 *      - Then we can use startWith operator to let other subscribers read from the cache & in case cache miss,
 *          generateNames() will be called.
 */
public class StartWithUseCase {

    private static final Logger log = LoggerFactory.getLogger(StartWithUseCase.class);

    // Caching the names
    private static final List<String> redis = new ArrayList<>();

    public static void main(String[] args) {

        //without__startWith();

        with__startWith();

    }

    private static void with__startWith() {
        generateNames_cached()
                .take(2)
                .subscribe(DefaultSubscriber.create("sam-sub1"));

        generateNames_cached()
                .take(2)
                .subscribe(DefaultSubscriber.create("mic-sub2"));

        // Here 2 names will be picked from cache & 1 name will be generated
        generateNames_cached()
                .take(3)
                .subscribe(DefaultSubscriber.create("jac-sub3"));

        generateNames_cached()
                .take(4)
                .subscribe(DefaultSubscriber.create("will-sub3"));
    }

    private static void without__startWith() {
        generateNames()
                .take(2)
                .subscribe(DefaultSubscriber.create("sam-sub1"));

        generateNames()
                .take(2)
                .subscribe(DefaultSubscriber.create("mic-sub2"));

        generateNames()
                .take(2)
                .subscribe(DefaultSubscriber.create("jac-sub3"));
    }

    private static Flux<String> generateNames_cached() {
        return Flux.generate(sink -> {
                    log.info("Generating name...");
                    Util.sleepSeconds(1);
                    var name = Util.faker().name().firstName();
                    redis.add(name);
                    sink.next(name);
                })
                .startWith(redis)
                .cast(String.class);
    }

    private static Flux<String> generateNames() {
        return Flux.generate(sink -> {
                    log.info("Generating name...");
                    Util.sleepSeconds(1);
                    var name = Util.faker().name().firstName();
                    sink.next(name);
                })
                .cast(String.class);
    }
}
