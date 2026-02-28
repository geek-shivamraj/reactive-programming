package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Flux.generate() accepts/gives a Consumer<SynchronousSink<T>> while Flux.create() accpets/gives a Consumer<FluxSink<T>>
 *  - Using SynchronousSink we can emit at max only one value, it will not allow to emit multiple values.
 *      - So, if we comment-out synchronousSink.next(2); it will not give any exception.
 *
 * 	- Let's understand,
 * 		- In Flux.create(),
 * 			- We will get fluxSink object (i.e., we as dev have complete control), we can create a for loop, share it with multiple
 * 		    	threads & can do whatever we want with fluxSink object.
 * 			- We can keep emitting the items without worrying about the downstream demand (irrespective of whether subscriber requests or not).
 * 			- When we used fluxSink.onRequest(), we controlled the loop i.e., we have to do everything to emit items
 *
 * 		- Flux.generate() is completely opposite to Flux.create().
 * 			- We will get synchronousSink object using which we're only allowed to emit 1 value, in case we want to emit more value,
 * 		    	then for each value, Flux.generate() will give us new synchronousSink object.
 * 			- i.e., Flux.generate() will be invoked again & again based on the downstream demand.
 * 			- In example below, you can see "Invoked…" is getting printed again & again i.e., every time Flux.generate()
 * 		    	is giving new synchronousSink object to emit one value.
 * 			- Flux.generate() takes the responsibility of looping the request on their side & whatever we want to execute,
 * 		    	we give inside the lambda expression.
 *          - Here we don't have to worry about the downstream demand, how many time the logic will execute will be handled by Reactor.
 *
 *  - Summary about Flux.generate()
 * 	    - It will invoke the given lambda expression again & again based on the downstream demand.
 * 	    - It can emit only one value at a time.
 *      - It will stop when complete()/error() method is invoked or if downstream cancels the subscription.
 *
 */
public class FluxGenerateDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxGenerateDemo.class);

    public static void main(String[] args) {

        Flux.generate(synchronousSink -> {
            log.info("Invoked...");
            synchronousSink.next(1);
            //synchronousSink.next(2); // This will give exception: More than one call to onNext
            //synchronousSink.complete(); // If we comment out this, it will keep on emitting value as 1
        });
                //.subscribe(DefaultSubscriber.create("sub1"));


        // Let's use take() operator to limit the downstream demand to 4 requests.
        Flux.generate(synchronousSink -> {
            log.info("Invoked...");
            synchronousSink.next(1);
            //synchronousSink.next(2); // This will give exception: More than one call to onNext
            //synchronousSink.complete(); // If we comment out this, it will keep on emitting value as 1
            //synchronousSink.error(new RuntimeException("oopss"));
        }).take(4).subscribe(DefaultSubscriber.create("sub1"));

    }
}
