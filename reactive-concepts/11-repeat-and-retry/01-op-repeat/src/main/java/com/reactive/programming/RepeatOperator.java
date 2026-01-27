package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *  Use case
 *      - We can use repeat operator to call the external service again & again to get latest update.
 *
 */
public class RepeatOperator {

    private static final Logger log = LoggerFactory.getLogger(RepeatOperator.class);

    public static void main(String[] args) {

        /**
         * Way 3: Using repeat operator to resubscribe mono after completion to emit values
         *  - Once we add repeat() operator to Mono, it becomes flux i.e., backpressure will be handled automatically unlike loop.
         */
        //repeat_option1();
        //repeat_option2();
        //repeat_option3();
        repeat_option4();

        Util.sleepSeconds(20);

        /**
         *  Way 1: To resubscribe mono to emit values
         */
        // getCountryName().subscribe(subscriber);
        // getCountryName().subscribe(subscriber);

        /**
         * Way 2: Using for-loop to resubscribe mono to emit values
         *  - This seems to work coz here everything is in memory.
         *  - Problem: If we use real non-blocking IO operation, then even before we get the response for the 1st iteration,
         *    the 2nd, 3rd iteration will start.
         *  - Solution: Use repeat operator as it will resubscribe the publisher only if the publisher emits onComplete signal in previous iteration.
         *      - i.e., repeat will be running in sequence like one by one (not concurrently).
         */
        //for (int i = 0; i < 3; i++) {
        //   getCountryName().subscribe(subscriber);
        //}


    }

    private static void repeat_option1() {

        // repeat() will again & again (indefinitely) resubscribe the publisher only if the publisher emits onComplete signal.
        getCountryName()
                .repeat()
                //.subscribe(DefaultSubscriber.create("sub1"))
        ;

        // repeat() will respect the cancel signal from the subscriber.
        getCountryName()
                .repeat()
                .takeUntil(c -> c.equalsIgnoreCase("canada"))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // repeat(3) will resubscribe the publisher 3 times in addition to the first subscription.
    private static void repeat_option2() {
        getCountryName()
                .repeat(3)
                .subscribe(DefaultSubscriber.create("sub2"));
    }

    private static void repeat_option3() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        getCountryName()
                .repeat(() -> atomicInteger.incrementAndGet() < 3)
                .subscribe(DefaultSubscriber.create("sub3"));
    }

    private static void repeat_option4() {
        getCountryName()
                .repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(2)))
                //.subscribe(DefaultSubscriber.create("sub4"))
        ;

        // repeat after every 2 seconds but not more than 3 times
        getCountryName()
                .repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(2)).take(3))
                .subscribe(DefaultSubscriber.create("sub4"));
    }

    private static Mono<String> getCountryName() {
        return Mono.fromSupplier(() -> Util.faker().country().name());
    }

}
