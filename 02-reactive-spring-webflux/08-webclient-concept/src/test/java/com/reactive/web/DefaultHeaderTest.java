package com.reactive.web;

import com.reactive.web.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class DefaultHeaderTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    public void defaultHeader() {
        client.get().uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print()).then()
                .as(StepVerifier::create)
                .expectComplete().verify();
    }

    @Test
    public void overrideHeader() {
        client.get().uri("/lec04/product/{id}", 1)
                .header("caller-id", "new-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print()).then()
                .as(StepVerifier::create)
                .expectComplete().verify();
    }

    @Test
    public void headersWithMap() {
        var map = Map.of(
          "caller-id", "new-value",
          "some-key", "some-value"
        );

        client.get().uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print()).then()
                .as(StepVerifier::create)
                .expectComplete().verify();
    }
}
