package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecase.Order;
import com.reactive.programming.usecase.OrderService;
import com.reactive.programming.usecase.User;
import com.reactive.programming.usecase.UserService;
import reactor.core.publisher.Flux;

/**
 *  FlatMap - How it works
 *      - We will observe that the flatMap operator is trying to subscribe to all the inner publishers at the same time.
 *      - flatMap operator will not wait for the 1st flux to complete before subscribing to the 2nd flux.
 *      - flatMap operator will try to subscribe to all the inner publishers at the same time. This behavior is very similar
 *          to merge() operator.
 *          e.g., OrderService.getUserOrders(1)
 *                      .mergeWith(OrderService.getUserOrders(2))
 *                      .mergeWith(OrderService.getUserOrders(3))
 *                      ....
 *      - So, using this flatMap() operator, we can send multiple concurrent requests. Default max limit = 256 (comes from the reactor queue size).
 *          - If we want to change the default limit, flatMap() operator accepts an additional parameter
 *              e.g., flatMap(OrderService::getUserOrders, 16 --> concurrency)
 */
public class FluxFlatMapOperator {

    public static void main(String[] args) {

        /**
         *  Get all the orders from order services
         */

        Flux<Flux<Order>> fluxOfFluxOrders = UserService.getAllUsers()
                        .map(user -> OrderService.getUserOrders(user.id()));
        fluxOfFluxOrders.subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("------------------------------------------------------------------------------");

        Flux<Order> fluxOfOrders = UserService.getAllUsers()
                .map(User::id)
                //.flatMap(OrderService::getUserOrders, 1);     // concurrency = 1
                .flatMap(OrderService::getUserOrders);

        fluxOfOrders.subscribe(DefaultSubscriber.create("sub2"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);
    }
}
