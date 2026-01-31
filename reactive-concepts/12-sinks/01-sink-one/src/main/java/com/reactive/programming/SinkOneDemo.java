package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

/**
 *
 */
public class SinkOneDemo {

    private static final Logger log = LoggerFactory.getLogger(SinkOneDemo.class);

    public static void main(String[] args) {

        // exploring sink methods to emit item / empty / error
        //demo1();

        // we can have subscribers for the same mono
        //demo2();

        // tryEmitValue Vs emitValue
        demo3();

    }

    // exploring sink methods to emit item / empty / error
    private static void demo1() {
        var sink = Sinks.one();
        var mono = sink.asMono();
        mono.subscribe(DefaultSubscriber.create("sub1"));

        //sink.tryEmitValue("Hello Sink One!!");

        //sink.tryEmitEmpty();

        sink.tryEmitError(new RuntimeException("Something went wrong!"));
    }

    // we can have subscribers for the same mono
    private static void demo2() {
        var sink = Sinks.one();
        var mono = sink.asMono();

        sink.tryEmitValue("Hello Sink One!!");  // Hot publisher

        mono.subscribe(DefaultSubscriber.create("sub1"));
        mono.subscribe(DefaultSubscriber.create("sub2"));
        mono.subscribe(DefaultSubscriber.create("sub3"));
    }

    // tryEmitValue Vs emitValue
    private static void demo3() {
        var sink = Sinks.one();
        var mono = sink.asMono();

        mono.subscribe(DefaultSubscriber.create("sub1"));

        sink.emitValue("Hello Sink One!!", (signalType, emitResult) -> {
            log.info("******** Sink One Value ********");
            log.info(signalType.name());
            log.info(emitResult.name());
            return false;
        });

        // Since the above is mono-type sink, we already emitted a value above & sent the complete signal
        // So, we're not supposed to emit any more value.
        // So, we can use this EmitFailureHandler arg of emitValue() method to handle the error & retry
        sink.emitValue("Hello Sink Two!!", (signalType, emitResult) -> {
            log.info("******** Sink Two Value ********");
            log.info("Signal Type: {}", signalType.name());
            log.info("Emit Result: {}", emitResult.name());
            return false;   // this boolean flag is for retry.
            //return true;    // this boolean flag is for retry indefinitely.
        });

    }

}
