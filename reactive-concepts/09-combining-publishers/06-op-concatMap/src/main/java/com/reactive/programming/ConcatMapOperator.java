package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecase.OrderService;
import com.reactive.programming.usecase.User;
import com.reactive.programming.usecase.UserService;

/**
 *  concatMap operator
 *      - Like flatMap, we've concatMap operator. Internally it will behave like below i.e., sequential
 *          e.g., OrderService.getUserOrders(1)
 *                      .concatWith(OrderService.getUserOrders(2))
 *                      .concatWith(OrderService.getUserOrders(3))
 *                      ....
 */
public class ConcatMapOperator {

    public static void main(String[] args) {

        UserService.getAllUsers()
                .map(User::id)
                .flatMap(OrderService::getUserOrders)
                .subscribe(DefaultSubscriber.create("sub1"));

        Util.sleepSeconds(3);

        System.out.println("<----------------------------------------------------------------------->");

        UserService.getAllUsers()
                .map(User::id)
                .concatMap(OrderService::getUserOrders)
                .subscribe(DefaultSubscriber.create("sub2"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }
}
