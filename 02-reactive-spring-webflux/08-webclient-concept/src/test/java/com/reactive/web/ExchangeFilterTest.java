package com.reactive.web;

import com.reactive.web.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

@Slf4j
public class ExchangeFilterTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator()).filter(requestLogger()));

    @Test
    public void exchangeFilter() {
        for (int i = 1; i <= 5; i++) {
            client.get().uri("/lec09/product/{id}", i)
                    .attribute("enable-logging", i % 2 == 0)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print()).then()
                    .as(StepVerifier::create)
                    .expectComplete().verify();
        }
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (request, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("Generated token: {}", token);
            var modifiedRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        };
    }

    private ExchangeFilterFunction requestLogger() {
        return (request, next) -> {
            var isEnabled = (Boolean) request.attributes().getOrDefault("enable-logging", false);
            if(isEnabled) {
                log.info("request url - {}: {}", request.method(), request.url());
            }
            return next.exchange(request);
        };
    }
}
