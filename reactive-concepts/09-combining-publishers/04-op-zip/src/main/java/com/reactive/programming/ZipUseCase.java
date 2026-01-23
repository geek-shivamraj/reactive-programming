package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecases.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UseCase:
 * - We've 3 different services like ProductNameService, ReviewService, PriceService & we want to combine them
 * to show the final product details.
 *
 */
public class ZipUseCase {

    private static final ExternalServiceClient externalServiceClient = new ExternalServiceClient();

    public static void main(String[] args) {

        /*
        for (int i = 1; i < 10; i++) {
            getProductDetails(i)
                    .subscribe(DefaultSubscriber.create("sub1"));
        }
        */

        Flux.range(1, 10)
                .flatMap(ZipUseCase::getProductDetails)
                .subscribe(DefaultSubscriber.create("sub2"));

        Util.sleepSeconds(5);
    }

    private static Mono<Product> getProductDetails(Integer productId) {
        return Mono.zip(
                        externalServiceClient.getProductNames(productId),
                        externalServiceClient.getReview(productId),
                        externalServiceClient.getPrice(productId)
                )
                .map(t -> new Product(t.getT1(), t.getT2(), t.getT3()));
    }

}
