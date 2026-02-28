package com.reactive.programming.error.handling;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Imp. Note:
 *      - Position of an operator is very important to clearly use the functionality as accordingly the operator's publisher
 *          & subscriber would vary.
 *
 *  - onErrorReturn
 *      - If we don't want to receive the exception, rather provide a hardcoded fallback value using onErrorReturn
 *      - We can use onErrorReturn overloaded method to provide a hardcoded fallback value based on the exception type.
 *
 */
public class OnErrorReturnOp {

    private static final Logger log = LoggerFactory.getLogger(OnErrorReturnOp.class);

    public static void main(String[] args) {

        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .subscribe(DefaultSubscriber.create());

        System.out.println("----------------------------------------------------------------------------------");

        // If we don't want to receive the exception, rather provide a fallback value using onErrorReturn
        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .onErrorReturn(ArithmeticException.class, -1)
                .onErrorReturn(IllegalArgumentException.class, -2)
                .onErrorReturn(-3)  // Generic fallback value
                .subscribe(DefaultSubscriber.create());
    }
}
