package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class DefaultSubscriberDemo {

    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriberDemo.class);

    public static void main(String[] args) {

        var mono = Mono.just(1);

        mono.subscribe(DefaultSubscriber.create("sub1"));
        mono.subscribe(DefaultSubscriber.create("sub2"));

    }
}
