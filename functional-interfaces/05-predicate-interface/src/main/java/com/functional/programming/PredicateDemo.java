package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

public class PredicateDemo {

    private static final Logger log = LoggerFactory.getLogger(PredicateDemo.class);

    public static void main(String[] args) {

        Predicate<Integer> isEven = n -> n % 2 == 0;
        log.info("{}", isEven.test(4)); // true
        log.info("{}", isEven.test(7)); // false

        // Filtering using Predicate
        List<String> names = List.of("India", "China", "USA", "Ireland", "Iceland");
        Predicate<String> startsWithS = s -> s.startsWith("I");
        names.stream().filter(startsWithS).forEach(log::info);

        // Chaining Predicates
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> divisibleBy3 = n -> n % 3 == 0;
        Predicate<Integer> positiveAndEven = isPositive.and(divisibleBy3);

        log.info("{}", positiveAndEven.test(9)); // true
        log.info("{}", positiveAndEven.test(-2)); // false

    }
}
