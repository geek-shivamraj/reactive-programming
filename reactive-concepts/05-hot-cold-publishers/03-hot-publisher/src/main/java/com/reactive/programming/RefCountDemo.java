package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - share() operator is used to convert a cold publisher to a hot publisher.
 *      - Shorthand for share() == publish().refCount(1)
 *      - It multicasts the source to all subscribers, meaning the source is subscribed to only once, and its emissions are shared among all subscribers.
 *      - Hot behavior: New subscribers only see items emitted after they subscribe. They don’t get a replay of past signals.
 *
 *      - flux.publish().refCount(1) means this Publisher needs minimum one subscriber to start emitting items.
 *      - flux.publish().refCount(2) means this Publisher needs minimum two subscriber to start emitting items.
 *      - flux.publish().refCount(0) will give exception: java.lang.IllegalArgumentException: n > 0 required but it was 0
 *
 *  - Refer ResubscriptionDemo.java for a special case called Resubscription.
 *
 */
public class RefCountDemo {

    private static final Logger log = LoggerFactory.getLogger(RefCountDemo.class);

    public static void main(String[] args) {

        //var movieStream = movieStream();  // Cold Publisher
        //var movieStream = movieStream().share();  // Hot Publisher
        //var movieStream = movieStream().publish().refCount(0);  // Hot Publisher

        // This movieStream flux will wait for both sub1 & sub2 to subscribe before starting to emit items
        var movieStream = movieStream().publish().refCount(2);  // Hot Publisher

        // Wait for 2 seconds for the sub1 - sam to subscribe
        Util.sleepSeconds(2);

        movieStream.take(5).subscribe(DefaultSubscriber.create("sub1 - sam"));

        Util.sleepSeconds(3);

        movieStream.take(4).subscribe(DefaultSubscriber.create("sub2 - mike"));

        // Stop the main thread for 15 seconds
        Util.sleepSeconds(15);
    }

    // movie theater
    private static Flux<String> movieStream() {
        return Flux.generate(
                        () -> {
                            log.info("********* Received the request for movie stream *********");
                            return 1;
                        },
                        (state, sink) -> {
                            var scene = "movie scene " + state;
                            log.info("Playing {}", scene);
                            sink.next(scene);
                            return ++state;
                        }
                ).take(5)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
