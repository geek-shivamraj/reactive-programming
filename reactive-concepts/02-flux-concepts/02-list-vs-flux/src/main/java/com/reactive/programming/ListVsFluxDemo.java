package com.reactive.programming;

import com.reactive.programming.pubsub.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  - Here we're going to implement a business requirement in 2 different styles
 *      1. Using List (Traditional Approach)
 *      2. Using Flux (Reactive Approach)
 *  - Business requirement: Generate random first names
 *      - Assume each time name generation is an expensive operation. Takes 1 second
 *
 *  - With Flux, we can stop early by cancelling the subscription but with list, we can't.
 *
 *  - With Traditional approach
 *      - In the traditional approach,we will be asking our name generator to generate names,
 *          it may take 5 mins or 5 hours to generate all the names & then it will give it to us.
 *  - With Reactive approach
 *      - In the reactive approach, we will be asking our name generator to generate names,
 *          it will generate names one by one & give it to us as and when it generates
 *          i.e., user doesn't have to wait for all the names to be generated.
 *      - Since, we've something to see, we can also react early or immediately
 *          i.e., we can stop the name generation process as soon as we get the 3rd name
 */
public class ListVsFluxDemo {

    private static final Logger log = LoggerFactory.getLogger(ListVsFluxDemo.class);

    public static void main(String[] args) {

        var list = NamesGenerator.generateNamesList(5);
        log.info("Generated List: {}", list);

        log.info("----------------------------------------------------");

        var flux = NamesGenerator.generateNamesFlux(5);
        flux.subscribe(DefaultSubscriber.create("sub1"));

        log.info("----------------------------------------------------");
        // Instead of Default subscriber, we can use pubsub subscriber to get subscription object
        var subscriber = new SubscriberImpl();
        NamesGenerator.generateNamesFlux(5)
                .subscribe(subscriber);
        subscriber.getSubscription().request(3);

        // suppose we like the 3rd generated name, we can stop early by cancelling the subscription
        log.info("Cancelling the subscription");
        subscriber.getSubscription().cancel();
    }
}


