package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - By default, Flux.generate() is a cold publisher coz
 *      - Per - Subscriber execution: Each time a subscriber subscribes, Reactor re‑invokes the Flux.generate() callback i.e., every subscriber
 *          gets its own independent sequence of emissions.
 *      - State handling: - State handling: Flux.generate() is designed for synchronous, stateful generation. You can maintain state across emissions,
 *          but that state is scoped to the subscriber’s run.
 *      - No shared stream: Since the callback is re‑executed for each subscriber, subscribers don’t share a single “live” stream.
 *          They each see their own generated sequence.
 *
 *  - share() operator is used to convert a cold publisher to a hot publisher.
 *      - Shorthand for publish().refCount(1)
 *      - It multicasts the source to all subscribers, meaning the source is subscribed to only once, and its emissions are shared among all subscribers.
 *      - Hot behavior: New subscribers only see items emitted after they subscribe. They don’t get a replay of past signals.
 *
 *      Case 1: Second Subscriber joins after 3 seconds of 1st subscriber & watch the movie till the end
 *          - Refer HotPublisher.java
 *          - Second sub will start watching from scene 4 (as the movie stream is shared among the subs)
 *
 *      case 2: First subscriber left before second subscriber joined (ReSubscription)
 *          - In this case, the movie stream will start from 1 for second subscriber
 *
 *      Case 3: First subscriber left after 5 scenes
 *          - Even after 1st sub left, the movie stream will continue as the 2nd sub is still watching
 *
 *      Case 4: Both subscribers left before the final scenes
 *          - The movie stream will stop if no subs are left to watch (even if whole movie is not completed)
 *
 *  - Note: When there is no subscriber left to the Hot Publisher, the publisher will stop emitting items (even if it has more items to emit)
 *
 */
public class HotPublisherCases {

    private static final Logger log = LoggerFactory.getLogger(HotPublisherCases.class);

    public static void main(String[] args) {

        //var movieStream = movieStream();  // Cold Publisher
        var movieStream = movieStream().share();  // Hot Publisher

        //movieStream.subscribe(DefaultSubscriber.create("sub1 - sam"));  // case 1
        //movieStream.take(1).subscribe(DefaultSubscriber.create("sub1 - sam")); // case 2
        movieStream.take(5).subscribe(DefaultSubscriber.create("sub1 - sam")); // case 3, 4

        Util.sleepSeconds(3);

        //movieStream.subscribe(DefaultSubscriber.create("sub2 - mike"));   // case 1
        movieStream.take(4).subscribe(DefaultSubscriber.create("sub2 - mike")); // case 2, 3, 4

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
