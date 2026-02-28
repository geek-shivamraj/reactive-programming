package com.reactive.web;

import com.reactive.web.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 *  - flatMap() operator is used to perform concurrent requests. Default max is 256
 *      - i.e., we can make 256 concurrent requests using flatMap()
 *      - So, for max = 250, it will take 5 seconds to complete all requests
 *      - For max = 260, it will take 10 seconds to complete all requests as other 4 requests will wait in queue
 *  - We can adjust the concurrency in flatMap() operator by customizing the second parameter as max.
 *      - i.e., flatMap(this::getProduct, max)
 *      - So, now for max = 260, it will take 5 seconds to complete all requests
 *      - For max = 499, it will take 5 seconds to complete all requests.
 *
 *  - Beyond max = 499 i.e., 500... we may get connection refused error as we've reached the OS kernel backlog size limit.
 */
public class DefaultConfigConnectionPoolTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void concurrentRequests() {
        //var max = 250; // 5 seconds
        //var max = 260;
        var max = 499;
        Flux.range(1, max)
                //.flatMap(this::getProduct)
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
