package com.reactive.programming.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends  AbstractHttpClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient.class);

    public Mono<String> getProductNames(int productId) {

        return this.httpClient.get()
                .uri("/demo01/product/" + productId)
                .responseContent().asString()
                .doOnNext(m -> log.info("next: {}", m))
                .next();
    }
}
