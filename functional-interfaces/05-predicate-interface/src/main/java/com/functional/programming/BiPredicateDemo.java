package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiPredicate;

public class BiPredicateDemo {

    private static final Logger log = LoggerFactory.getLogger(BiPredicateDemo.class);

    public static void main(String[] args) {

        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
        log.info("{}", isGreater.test(10, 5));  // true
        log.info("{}", isGreater.test(3, 7));   // false

        // String comparison
        BiPredicate<String, String> startsWith = (str, prefix) -> str.startsWith(prefix);
        log.info("{}", startsWith.test("Shivam", "Shi")); // true
        log.info("{}", startsWith.test("Shivam", "Am"));  // false

        // Filtering with streams
        List<String> names = List.of("India", "China", "USA", "Ireland", "Iceland");
        BiPredicate<String, Integer> longerThan = (name, length) -> name.length() > length;
        names.stream().filter(name -> longerThan.test(name, 4)).forEach(log::info);

    }
}
