package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BinaryOperator;

public class BinaryOperatorDemo {

    private static final Logger log = LoggerFactory.getLogger(BinaryOperatorDemo.class);

    public static void main(String[] args) {

        BinaryOperator<Integer> add = (a, b) -> a + b;
        log.info("Addition of 5 and 10 is {}", add.apply(5, 10));

        // String transformation
        BinaryOperator<String> concat = (s1, s2) -> s1 + " " + s2;
        log.info("Concatenation of hello and world is {}", concat.apply("hello", "world"));

        // Using with streams (reduction)
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        BinaryOperator<Integer> sum = (a, b) -> a + b;
        int result = numbers.stream().reduce(0, sum);
        log.info("Sum of numbers is {}", result);
    }
}
