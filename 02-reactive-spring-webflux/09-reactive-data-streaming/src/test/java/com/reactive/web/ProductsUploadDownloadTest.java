package com.reactive.web;

import com.reactive.web.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.time.Duration;

@Slf4j
public class ProductsUploadDownloadTest {

    private final ProductClient productClient = new ProductClient();

    @Test
    public void upload() {

//        var flux = Flux.just(new ProductDto(null, "iphone", 1000))
//                        .delayElements(Duration.ofSeconds(10));

        var flux = Flux.range(1, 10)
                .map(i -> new ProductDto(null, "product-" + i, i))
                .delayElements(Duration.ofSeconds(2));

        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void millionProductUpload() {

        var flux = Flux.range(1, 1_000_000)
                .map(i ->
                        new ProductDto(null, "product-" + i, i));

        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("Received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void download() {
        productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux ->
                        FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void bidirectionalProductUpload() {

        var flux = Flux.range(1, 10)
                .map(i ->
                        new ProductDto(null, "product-" + i, i))
                .delayElements(Duration.ofSeconds(2));

        productClient.bidirectionalUploadProducts(flux)
                .doOnNext(r -> log.info("Received Product: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
