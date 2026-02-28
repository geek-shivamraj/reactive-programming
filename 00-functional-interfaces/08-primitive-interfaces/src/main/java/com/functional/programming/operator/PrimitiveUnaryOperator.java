package com.functional.programming.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

public class PrimitiveUnaryOperator {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveUnaryOperator.class);

    public static void main(String[] args) {

        IntUnaryOperator square = i -> i * i;
        log.info("{}", square.applyAsInt(5)); // 25

        LongUnaryOperator doubleLong = l -> l * 2;
        log.info("{}", doubleLong.applyAsLong(10L)); // 20

        DoubleUnaryOperator half = d -> d / 2;
        log.info("{}", half.applyAsDouble(8.0)); // 4.0
    }
}
