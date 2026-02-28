package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecase.Order;
import com.reactive.programming.usecase.OrderService;
import com.reactive.programming.usecase.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  Independent producers
 *      - startWith, concat, merge, zip
 *
 *  Dependent sequential non-blocking IO calls!
 *      - flatMap, flatMapMany
 *      - flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
 *      - Mono is supposed to be 1 item - What if the inner publisher emits multiple items?
 *
 */
public class FlatMapManyOperator {

    public static void main(String[] args) {

        /**
         *  We've usernames & we need to get user orders
         */

        Mono<Flux<Order>> monoFlux =UserService.getUserId("sam")
                .map(OrderService::getUserOrders);

        monoFlux.subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("------------------------------------------------------------------------------");

        Flux<Order> flattenMonoFlux =UserService.getUserId("mike")
                .flatMapMany(OrderService::getUserOrders);

        flattenMonoFlux.subscribe(DefaultSubscriber.create("sub2"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }
}
