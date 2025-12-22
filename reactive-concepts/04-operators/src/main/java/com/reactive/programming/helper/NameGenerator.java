package com.reactive.programming.helper;

import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class NameGenerator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    @Override
    public void accept(FluxSink<String> fluxSink) {
        this.sink = fluxSink;
    }

    public void generate() {
        sink.next(Util.faker().country().name());
    }
}
