package com.functional.programming.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

public class PrimitiveBinaryOperator {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveBinaryOperator.class);

    public static void main(String[] args) {

        IntBinaryOperator add = (a, b) -> a + b;
        log.info("{}", add.applyAsInt(5, 10)); // 15

        LongBinaryOperator multiply = (a, b) -> a * b;
        log.info("{}", multiply.applyAsLong(5L, 10L)); // 50

        DoubleBinaryOperator divide = (a, b) -> Math.max(a, b);
        log.info("{}", divide.applyAsDouble(2.5, 3.7)); // 3.7
    }
}
