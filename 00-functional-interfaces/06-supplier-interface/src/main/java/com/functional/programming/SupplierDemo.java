package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class SupplierDemo {

    private static final Logger log = LoggerFactory.getLogger(SupplierDemo.class);

    public static void main(String[] args) {

        Supplier<String> supplier = () -> "Hello, Shivam!";
        log.info("{}", supplier.get());

        // Random Number Generator
        Supplier<Double> randomSupplier = () -> Math.random();
        log.info("{}", randomSupplier.get()); // e.g., 0.374

        // Object Factory
        Supplier<StringBuilder> builderSupplier = () -> new StringBuilder();
        StringBuilder sb = builderSupplier.get();
        sb.append("Supplier created this!");
        log.info("{}", sb);

        Stream.generate(() -> Math.random())
                .limit(5)
                .forEach(i -> log.info("{}", i));

    }
}
