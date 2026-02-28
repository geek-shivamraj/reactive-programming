package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.UnaryOperator;

public class UnaryOperatorDemo {

    private static final Logger log = LoggerFactory.getLogger(UnaryOperatorDemo.class);

    public static void main(String[] args) {

        UnaryOperator<Integer> square = x -> x * x;
        log.info("Square of 5 is {}", square.apply(5));

        // String transformation
        UnaryOperator<String> toUpperCase = s -> s.toUpperCase();
        log.info("toUpperCase of hello is {}", toUpperCase.apply("hello"));

        // Using with Collections
        List<String> names = List.of("India", "China", "USA", "Russia", "Germany");
        List<String> mutableNames = new java.util.ArrayList<>(names);

        // List.replaceAll(UnaryOperator) uses it to transform each element in place.
        mutableNames.replaceAll(s -> s.toUpperCase());
        log.info("Names in upper case: {}", mutableNames);
    }
}
