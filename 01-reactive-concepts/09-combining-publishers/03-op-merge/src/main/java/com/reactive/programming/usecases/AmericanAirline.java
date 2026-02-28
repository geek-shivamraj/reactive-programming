package com.reactive.programming.usecases;

import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class AmericanAirline {

    private static final String AIRLINE = "AmericanAirline";

    public static Flux<Flight> getFlights(){
     return Flux.range(1, Util.faker().random().nextInt(5, 10))
             .delayElements(Duration.ofMillis(Util.faker().random().nextInt(200, 1200)))
             .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(300, 1200)))
             .transform(Util.fluxLogger(AIRLINE));
    }
}
