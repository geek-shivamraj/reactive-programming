package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import org.apache.commons.lang3.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  - This is a non-blocking IO, main thread exists immediately. Hence, we will block the main thread for 2 seconds.
 */
public class NonBlockingIODemo {

    public static final Logger log = LoggerFactory.getLogger(NonBlockingIODemo.class);

    public static void main(String[] args) {
        var client = new ExternalServiceClient();

        log.info("Calling External Service....");
        client.getProductName(1);
                //.subscribe(DefaultSubscriber.create("sub1"));

        /**
         *
         *  Let's send 5 requests i.e., 5 publisher with different product id & we subscribe every single publisher
         *      - Assumption, external service takes 1 seconds to respond to the request.
         *      - Also, notice that we don't have 5 threads, just one single thread.
         *      - Remember this is a non-blocking IO, so we send all 5 requests more or less at the same time & once the request is sent
         *          to the network, anything could happen in the n/w.
         *      - It all depends on when the remote service get the request & when it processes & respond back to use.
         *      - In what order we will receive the response to the requests, we can't be sure.
         *      - We can maintain the order of the requests. We will learn later.
         *
         *  - We can test this for 50 -> 100 requests & we will get all 50 responses with just one single thread.
         *      - In traditional programming to get 100 product info & assuming each request takes 1 second to respond,
         *          then the for loop will take 100 seconds to complete (with 1 thread) why?
         *          - Becoz the thread will wait for each request to process & complete before moving to the next request. So, it's blocked.
         *      - For all 100 request to process at the same time, we need 100 threads but in our case, we didn't need 100 seconds,
         *          we just took 1 second with just one thread.
         *
         */
        for(int id = 0; id < 100; id++) {
            client.getProductName(id)
                    .subscribe(DefaultSubscriber.create("sub1"));
        }

        Util.sleepSeconds(2);
    }
}
