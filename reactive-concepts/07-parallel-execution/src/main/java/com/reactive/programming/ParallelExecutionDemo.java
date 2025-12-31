package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ParallelExecutionDemo {

    private static final Logger log = LoggerFactory.getLogger(ParallelExecutionDemo.class);
    public static void main(String[] args) {

        Flux.range(1, 10)
                .parallel() // default parallelism (default number of CPU cores)
                //.parallel(3) // custom parallelism
                .runOn(Schedulers.parallel())
                .map(ParallelExecutionDemo::process)
                .map(i -> i + "a")
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(10);
    }

    private static int process(int i) {
        log.info("Time consuming task: {}", i);
        Util.sleepSeconds(1);
        return i * 2;
    }
}
