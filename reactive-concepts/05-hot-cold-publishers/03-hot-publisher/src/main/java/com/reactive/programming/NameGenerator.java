package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/**
 *  - Let's add log.info inside the accept() method, this log will be printed twice why ?
 *      - Log will be printed twice because we have two subscribers & for each subscriber, we will have one FluxSink.
 *      - But here we've created just one instance of NameGenerator, & that's what we've shared with multiple threads
 *
 */
public class NameGenerator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    public static final Logger log = LoggerFactory.getLogger(NameGenerator.class);

    @Override
    public void accept(FluxSink<String> fluxSink) {
        log.info("FluxSink initialized!!");
        this.sink = fluxSink;
    }

    public void generate() {
        sink.next(Util.faker().country().name());
    }
}
