package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - Almost same as publish().refCount(1) == share() with below differences
 *      - Publisher will not stop emitting items even if subscriber leaves/cancels. So, even with 0 subscriber, publisher will start producing items.
 *      - This is actually REAL HOT PUBLISHER - publish().autoConnect(0)
 *
 *  - publish().autoConnect() will start emitting items as soon as the first subscriber joins & will emit all the items even if there is no subscribers.
 *  - publish().autoConnect(0) will start emitting items without any subscriber.
 *
 */
public class AutoConnectDemo {

    private static final Logger log = LoggerFactory.getLogger(AutoConnectDemo.class);

    public static void main(String[] args) {

        // This will by default assumes minSubscribers = 1 to start emitting items.
        // Once started, it will emit all the items even if there is no subscribers.
        //var movieStream = movieStream().publish().autoConnect();

        // Here it will not wait for even the subscriber to join before starting to emit items.
        var movieStream = movieStream().publish().autoConnect(0);  // REAL HOT PUBLISHER

        // Wait for 3 seconds for the sub1 - sam to subscribe
        Util.sleepSeconds(2);

        movieStream.take(2).subscribe(DefaultSubscriber.create("sub1 - sam"));

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
                ).take(10)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
