package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  - Windowing in Reactor is like buffering, but instead of collecting items into a list (Flux<List<T>>), the window operator
 *      collects them into sub-Fluxes (Flux<Flux<T>>).
 *  - Each window is itself a reactive stream that can be consumed independently.
 *
 */
public class WindowingOperator {

    private static final Logger log = LoggerFactory.getLogger(WindowingOperator.class);

    public static void main(String[] args) {

        eventStream()
                //.window(8)
                .window(Duration.ofMillis(2000))
                .flatMap(WindowingOperator::processEvents)
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(60);
    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> "event - " + (i + 1));
    }

    private static Mono<Void> processEvents(Flux<String> flux) {
        return flux.doOnNext(e -> System.out.print("*"))
                .doOnComplete(System.out::println)
                .then();
    }


}
