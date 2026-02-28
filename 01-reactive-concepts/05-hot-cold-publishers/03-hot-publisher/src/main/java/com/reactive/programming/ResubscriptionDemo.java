package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - This is a special case of Hot Publisher where when the flux doesn't have any subscriber, it will stop emitting items.
 *      - So, In case a subscriber joins & leaves and after sometime another subscriber joins when the flux is not emitting items, then for the new subscriber,
 *          flux will start emitting items from the first again.
 *      - This special case is also known as Resubscription
 */
public class ResubscriptionDemo {

    private static final Logger log = LoggerFactory.getLogger(ResubscriptionDemo.class);

    public static void main(String[] args) {

        var movieStream = movieStream().share();

        // Wait for 2 seconds for the sub1 - sam to subscribe
        Util.sleepSeconds(2);

        // sub1 - sam will watch only 2 scenes & then leave the movie stream.
        movieStream.take(2).subscribe(DefaultSubscriber.create("sub1 - sam"));

        Util.sleepSeconds(3);

        movieStream.subscribe(DefaultSubscriber.create("sub2 - mike"));

        // Stop the main thread for 15 seconds
        Util.sleepSeconds(10);
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
