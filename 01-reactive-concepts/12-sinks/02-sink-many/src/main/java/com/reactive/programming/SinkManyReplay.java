package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

public class SinkManyReplay {

    private static final Logger log = LoggerFactory.getLogger(SinkManyReplay.class);

    public static void main(String[] args) {
        demo1();
    }

    private static void demo1() {

        // all the message will be stored in an unbounded queue.
        //var sink = Sinks.many().replay().all();

        // we can use limit() to let the late subscriber to see last 2 message or 2 minutes.
        var sink = Sinks.many().replay().limit(1);

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));

        sink.tryEmitNext("One");
        sink.tryEmitNext("Two");
        sink.tryEmitNext("Three");

        log.info("Sub3 joining in 2 seconds...");
        Util.sleepSeconds(2);

        flux.subscribe(DefaultSubscriber.create("sub3"));
        sink.tryEmitNext("Four");
    }
}
