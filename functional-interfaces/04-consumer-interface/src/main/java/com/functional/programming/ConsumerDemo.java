package com.functional.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) {

        Consumer<String> consumer = s -> log.info(s.toUpperCase());
        consumer.accept("Shivam");

        List<String> names = List.of("India", "China", "USA");
        names.forEach(log::info);
    }
}
