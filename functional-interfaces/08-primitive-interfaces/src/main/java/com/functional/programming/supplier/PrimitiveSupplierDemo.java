package com.functional.programming.supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

public class PrimitiveSupplierDemo {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveSupplierDemo.class);

    public static void main(String[] args) {

        IntSupplier randomInt = () -> (int) (Math.random() * 100);
        log.info("Random Int: {}", randomInt.getAsInt());

        LongSupplier currentTime = () -> System.currentTimeMillis();
        log.info("Random Long: {}", currentTime.getAsLong());

        DoubleSupplier randomDouble = () -> Math.random();
        log.info("Random Double: {}", randomDouble.getAsDouble());
    }
}
