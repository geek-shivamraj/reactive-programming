package com.functional.programming.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

public class PrimitiveFunctionDemo {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveFunctionDemo.class);

    public static void main(String[] args) {
        IntFunction<String> intToString = i -> "Number: " + i;
        log.info(intToString.apply(10)); // 10

        LongFunction<String> longToString = l -> "Long: " + l;
        log.info(longToString.apply(10L)); // 10

        DoubleFunction<String> doubleToString = d -> "Double: " + d;
        log.info(doubleToString.apply(10.5)); // 10.5
    }
}
