package com.reactive.programming.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class UnblockingServiceClient extends  AbstractHttpClient {

    private static final Logger log = LoggerFactory.getLogger(UnblockingServiceClient.class);

    public Mono<String> getProductNames(int productId) {

        return this.httpClient.get()
                .uri("/demo01/product/" + productId)
                .responseContent().asString()
                .doOnNext(m -> log.info("next: {}", m))
                .next()
                .publishOn(Schedulers.boundedElastic());
    }
}
