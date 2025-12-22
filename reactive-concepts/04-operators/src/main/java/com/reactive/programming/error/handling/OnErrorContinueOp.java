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
 *  - onErrorContinue
 *      - onErrorContinue is helpful to continue the stream in case exception is raised & we don't want to
 *          receive the exception.
 *
 */
public class OnErrorContinueOp {

    private static final Logger log = LoggerFactory.getLogger(OnErrorContinueOp.class);

    public static void main(String[] args) {

        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .onErrorContinue((ex, obj) -> log.error("===> Exception raised!!", ex))
                .subscribe(DefaultSubscriber.create("sub1"));
    }
}
