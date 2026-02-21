package com.reactive.web;

import com.reactive.web.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class HttpConnectionPoolingTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void concurrentRequests() throws InterruptedException {
        var max = 3;
        Flux.range(1, max)
                .flatMap(this::getProduct)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();

        Thread.sleep(Duration.ofMinutes(1));
    }

    private Mono<Product> getProduct(int id) {
        return client.get().uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
