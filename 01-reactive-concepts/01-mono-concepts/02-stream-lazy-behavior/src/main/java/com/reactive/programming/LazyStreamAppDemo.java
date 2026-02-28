package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 *      If we don't have the terminal operator, then stream operators will not execute.
 * <p>
 *      - Java 8 introduced Stream & by default stream has lazy behavior i.e., the stream will not execute till
 *      you connect a terminal operator & this is how Reactive program is going to be.
 * <p>
 *      - So, when we use Mono or Flux, we will be writing code in Functional style & unless we connect a subscriber/subscribe, it will not execute.
 */
public class LazyStreamAppDemo {

    private static final Logger log = LoggerFactory.getLogger(LazyStreamAppDemo.class);

    public static void main(String[] args) {
        Stream.of(1).peek(i -> log.info("Received: {}", i)).toList();
    }
}


