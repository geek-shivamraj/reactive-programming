package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecases.AmericanAirline;
import com.reactive.programming.usecases.Emirates;
import com.reactive.programming.usecases.Flight;
import com.reactive.programming.usecases.Qatar;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Use cases:
 * - Let's image we've an application like Flight Ticket Booking Application.
 * - If an end user searches for a flight, we need to search in multiple flight providers with same source to destination.
 *      we will wait for 2 seconds to get the results.
 * - In this case, we can use merge operator to merge the results from multiple flight providers/producers.
 */
public class MergeUseCases {

    public static void main(String[] args) {

        searchFlights()
                .subscribe(DefaultSubscriber.create("Kayak"));

        // Blocking the main thread
        Util.sleepSeconds(3);
    }

    public static Flux<Flight> searchFlights() {
        return Flux.merge(
                        AmericanAirline.getFlights(),
                        Emirates.getFlights(),
                        Qatar.getFlights()
                )
                .take(Duration.ofSeconds(2));
    }

}
