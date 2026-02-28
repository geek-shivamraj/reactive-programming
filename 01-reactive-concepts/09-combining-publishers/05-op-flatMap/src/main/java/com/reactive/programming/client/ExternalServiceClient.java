package com.reactive.programming.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends  AbstractHttpClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient.class);

    public Mono<String> getProductNames(int productId) {
        return get("/demo05/product/" + productId);
    }

    public Mono<String> getReview(int productId) {
        return get("/demo05/review/" + productId);
    }

    public Mono<String> getPrice(int productId) {
        return get("/demo05/price/" + productId);
    }

    private Mono<String> get(String path) {
        return this.httpClient.get()
                .uri(path)
                .responseContent().asString()
                .next();
    }
}
