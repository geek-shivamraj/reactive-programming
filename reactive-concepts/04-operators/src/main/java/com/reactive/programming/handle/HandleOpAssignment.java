package com.reactive.programming.handle;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;

/**
 *  - Assignment
 *      - Generate a Flux of random countries
 *      - If the country is Canada, complete the sequence
 *      - Otherwise emit the country
 */
public class HandleOpAssignment {

    public static void main(String[] args) {

        Flux<String> flux = Flux.<String>generate(sink -> sink.next(Util.faker().country().name()))
                .handle((item, sink) -> {
                   sink.next(item);
                   if(item.equalsIgnoreCase("canada")) {
                       sink.complete();
                   }
                });

        flux.subscribe(DefaultSubscriber.create("sub1"));
    }
}
