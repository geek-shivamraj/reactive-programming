package com.reactive.programming.client;

import reactor.core.publisher.Mono;

public class ExternalServiceClient extends  AbstractHttpClient {

    /**
     *  - We will be receiving the response from the remote server as ByteBuffer (ByteBuf) object
     *      (ByteBuf is a container of bytes & ByteBufFlux is a streaming byte array).
     *  - This is a bit low level, when we use WebFlux, it will simply all these things & we will not be even seeing these stuff.
     *  - We're using "next()" to convert the Flux to Mono (similar to stream method "findFirst()").
     */
    public Mono<String> getProductName(int productId) {
        return this.httpClient.get()
                .uri("/demo01/product/" + productId)
                .responseContent().asString().next();
    }
}
