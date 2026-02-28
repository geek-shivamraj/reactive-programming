package com.reactive.web.controller;

import com.reactive.web.dto.ProductDto;
import com.reactive.web.dto.UploadResponse;
import com.reactive.web.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *  Streaming Request
     */
    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> flux) {
        log.info("Invoked Upload API...");
        return productService.saveProducts(flux)
                .then(productService.getProductsCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    /**
     *  Streaming Response
     */
    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProducts() {
        log.info("Invoked Download API...");
        return productService.allProducts();
    }

    /**
     *  Streaming Request & Streaming Response (Bidirectional)
     */
    @PostMapping(value = "bidirectional/upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> bidirectionalCommunication(@RequestBody Flux<ProductDto> flux) {
        log.info("Invoked Bidirectional Upload API...");
        return productService.saveProducts(flux.doOnNext(r -> log.info("Received: {}", r)));
    }
}
