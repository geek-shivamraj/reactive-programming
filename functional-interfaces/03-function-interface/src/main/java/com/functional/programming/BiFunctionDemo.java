package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

/**
 *  - BiFunction<T, U, R> is a functional interface that represents a function that takes two input parameters and returns a result.
 *      - R apply(T t, U u);
 *      - T: Type of the first argument
 *      - U: Type of the second argument
 *      - R: Type of the result
 */
public class BiFunctionDemo {

    private static final Logger log = LoggerFactory.getLogger(BiFunctionDemo.class);

    public static void main(String[] args) {

        // Add two integers
        //BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<Integer, Integer, Integer> add = Integer::sum;
        log.info("Sum: {}", add.apply(10, 20)); // Output: 30

        // Concatenate two strings
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + " " + s2;
        log.info("Concat: {}", concat.apply("Hello", "Shivam")); // Output: Hello Shivam

    }
}
