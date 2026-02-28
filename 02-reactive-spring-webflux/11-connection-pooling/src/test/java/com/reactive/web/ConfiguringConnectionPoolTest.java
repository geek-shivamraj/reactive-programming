package com.reactive.web;

import com.reactive.web.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class ConfiguringConnectionPoolTest extends AbstractWebClient {

    private final WebClient client = createWebClient(builder -> {
        var poolSize = 5000;
        var provider = ConnectionProvider.builder("custom")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5)
                .build();

        var httpClient = HttpClient.create(provider)
                .compress(true)
                .keepAlive(true);

        builder.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() {
        var max = 5000;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(int id) {
        return client.get().uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
