package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotPublisher {

    private static final Logger log = LoggerFactory.getLogger(HotPublisher.class);

    public static void main(String[] args) {

        var movieStream = movieStream().share();

        Util.sleepSeconds(2);

        movieStream.subscribe(DefaultSubscriber.create("sam"));

        Util.sleepSeconds(3);

        movieStream.subscribe(DefaultSubscriber.create("mike"));

        // Stop the main thread for 15 seconds
        Util.sleepSeconds(15);
    }

    private static Flux<String> movieStream() {
        return Flux.generate(
                        () -> {
                            log.info("Received the request");
                            return 1;
                        },
                        (state, sink) -> {
                            var scene = "movie scene " + state;
                            log.info("Playing {}", scene);
                            sink.next(scene);
                            return ++state;
                        }
                ).take(10)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
