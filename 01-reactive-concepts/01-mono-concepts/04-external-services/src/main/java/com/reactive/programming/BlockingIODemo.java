package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  - This is a blocking IO, main thread will execute & will be blocked till the response
 *      is received from External service.
 */
public class BlockingIODemo {

    public static final Logger log = LoggerFactory.getLogger(BlockingIODemo.class);

    public static void main(String[] args) {
        var client = new ExternalServiceClient();

        log.info("Calling External Service using blocking IO....");

        for(int id = 0; id < 100; id++) {
            String response = client.getProductName(id)
                    .block();
                    //.subscribe(DefaultSubscriber.create("sub1"));
            log.info("[{}] Received item: {}", Thread.currentThread().getName(), response);
        }

        //Util.sleepSeconds(2);
    }
}
