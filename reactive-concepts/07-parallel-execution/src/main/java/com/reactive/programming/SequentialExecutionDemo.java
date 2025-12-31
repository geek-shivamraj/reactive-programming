package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class SequentialExecutionDemo {

    private static final Logger log = LoggerFactory.getLogger(SequentialExecutionDemo.class);
    public static void main(String[] args) {

        Flux.range(1, 10)
                .map(SequentialExecutionDemo::process)
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }

    private static int process(int i) {
        log.info("Time consuming task: {}", i);
        Util.sleepSeconds(1);
        return i * 2;
    }
}
