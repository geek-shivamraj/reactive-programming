package com.functional.programming.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class PrimitiveConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveConsumerDemo.class);

    public static void main(String[] args) {

        IntConsumer printSquare = i -> log.info("{}", i * i);
        printSquare.accept(5);  // 25

        LongConsumer printLong = l -> log.info("{}", l);
        printLong.accept(123L);  // 123

        DoubleConsumer printDouble = d -> log.info("{}", d);
        printDouble.accept(123.456);  // 123.456
    }
}
