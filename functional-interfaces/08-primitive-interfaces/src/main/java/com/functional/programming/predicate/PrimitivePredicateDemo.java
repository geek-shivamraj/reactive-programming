package com.functional.programming.predicate;

import com.functional.programming.function.PrimitiveFunctionDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public class PrimitivePredicateDemo {

    private static final Logger log = LoggerFactory.getLogger(PrimitivePredicateDemo.class);

    public static void main(String[] args) {
        IntPredicate isEven = n -> n % 2 == 0;
        log.info("Is 10 even? {}", isEven.test(10));    // true

        LongPredicate isPositive = n -> n > 0;
        log.info("Is 10 positive? {}", isPositive.test(-5L));   // false

        DoublePredicate isGreaterThanFive = d -> d > 1.0;
        log.info("Is 10 greater than 5? {}", isGreaterThanFive.test(10.5)); // true
    }
}
