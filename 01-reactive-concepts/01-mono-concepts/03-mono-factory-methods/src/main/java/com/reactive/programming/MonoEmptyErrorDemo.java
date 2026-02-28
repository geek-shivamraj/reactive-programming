package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 *  - The Publisher doesn't have to give items always, it can give only if items are available.
 *  - If items are not available, then the Publisher will directly invoke the onComplete() method to indicate it doesn't have data.
 *  - Similarly, in case of error, Publisher will directly invoke onError() method to indicate error & pass the error message.
 *
 *  Mono.empty()
 *      - We can use this to create publisher which is not going to give data.
 *  Mono.error()
 *      - We can use this to create publisher which is going to pass the error message.
 */
public class MonoEmptyErrorDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoEmptyErrorDemo.class);

    public static void main(String[] args) {

        getUsername(1).subscribe(DefaultSubscriber.create("sub1"));

        getUsername(2).subscribe(DefaultSubscriber.create("sub2"));

        getUsername(3).subscribe(DefaultSubscriber.create("sub3"));
    }

    private static Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("sam");
            case 2 -> Mono.empty();
            default -> Mono.error(new RuntimeException("Invalid user Id"));
        };
    }
}
