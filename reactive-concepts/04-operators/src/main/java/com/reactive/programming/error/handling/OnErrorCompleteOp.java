package com.reactive.programming.error.handling;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  - Imp. Note:
 *      - Position of an operator is very important to clearly use the functionality as accordingly the operator's publisher
 *          & subscriber would vary.
 *
 *  - onErrorComplete
 *      - onErrorComplete is helpful to complete the stream in case exception is raised & we don't want to
 *          receive the exception.
 *
 */
public class OnErrorCompleteOp {

    private static final Logger log = LoggerFactory.getLogger(OnErrorCompleteOp.class);

    public static void main(String[] args) {

        Flux.range(1, 10)
                .onErrorComplete()
                .subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("----------------------------------------------------------------------------------");

        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .onErrorComplete()
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
