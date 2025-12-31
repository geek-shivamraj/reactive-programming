package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SelectiveParallelExecution {

    private static final Logger log = LoggerFactory.getLogger(SelectiveParallelExecution.class);
    public static void main(String[] args) {

        Flux.range(1, 10)
                .parallel()         // default parallelism (default number of CPU cores)
                //.parallel(3)      // custom parallelism
                .runOn(Schedulers.parallel())
                .map(SelectiveParallelExecution::process)
                .sequential()      // After this step, every operator/operation will be executed/procesed sequentially.
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
