package com.reactive.web;

import com.reactive.web.dto.ProductDto;
import com.reactive.web.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080").build();

    /**
     *  Streaming Request
     */
    public Mono<UploadResponse> uploadProducts(Flux<ProductDto> flux) {
        return client.post().uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDto.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    /**
     *  Streaming Response
     */
    public Flux<ProductDto> downloadProducts() {
        return client.get().uri("/products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    /**
     *  Bidirectional Communication: Streaming Request & Response.
     */
    public Flux<ProductDto> bidirectionalUploadProducts(Flux<ProductDto> flux) {
        return client.post().uri("/products/bidirectional/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDto.class)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }
}
