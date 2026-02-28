package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiConsumer;

public class BiConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(BiConsumerDemo.class);

    public static void main(String[] args) {

        // Example 1: Print two values
        BiConsumer<String, Integer> biConsumer = (name, age) ->
                log.info("{} is {} years old", name, age);
        biConsumer.accept("Shivam", 25);

        // Example 2: Iterate over a Map
        Map<String, Integer> scores = Map.of("Math", 90, "Science", 85);
        scores.forEach((subject, score) ->
                log.info("{} -> {}", subject, score));
        // forEach internally uses BiConsumer

    }
}
