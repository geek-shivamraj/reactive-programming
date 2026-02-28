package com.reactive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("traditional")
public class TraditionalWebController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalWebController.class);
    private final RestClient restClient = RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl("http://localhost:7070").build();

    /**
     *
     *      // pseudo code to give you an idea
     *      var list = getProducts();   // synchronous & blocking
     *      channel.writeAndFlush(list);
     *
     */

    @GetMapping("products")
    public List<Product> getProducts() {
        var list = this.restClient.get()
                                  .uri("/demo01/products")
                                  .retrieve()
                                  .body(new ParameterizedTypeReference<List<Product>>() {
                                   });
        log.info("Received Response: {}", list);
        return list;
    }

    /**
     *  Common Mistake using Reactive Pipeline
     *   - This is not reactive even though we're returning Flux.
     *   - Here we're still writing synchronous blocking style code & at end, we're just sending the response via flux.
     *   - Just return Flux doesn't make the API as reactive
     *   - In order to have a reactive API, everything has to be part of the reactive pipeline. Here we're calling the
     *      external service outside the reactive pipeline.
     */
    @GetMapping("products/stream")
    public Flux<Product> getProductsNonReactive() {
        var list = this.restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("Received Stream Response: {}", list);
        return Flux.fromIterable(list);
    }

}
