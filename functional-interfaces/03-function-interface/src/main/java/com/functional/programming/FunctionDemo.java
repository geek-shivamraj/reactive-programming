package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 *  java.util.function.Function<T, R> - takes T as input and returns R
 *      - R apply(T t);
 *      - It’s part of the java.util.function package introduced in Java 8 for functional programming.
 */
public class FunctionDemo {

    private static final Logger log = LoggerFactory.getLogger(FunctionDemo.class);

    public static void main(String[] args) {

        Function<String, Integer> function = s -> s.length();
        log.info("{}", function.apply("Shivam"));

        //Function<String, String> toUpper = s -> s.toUpperCase();
        Function<String, String> toUpper = String::toUpperCase;
        Function<String, String> addExclaim = s -> s + "!";
        Function<String, String> combined = toUpper.andThen(addExclaim);
        log.info("{}", combined.apply("Hello"));
    }
}
