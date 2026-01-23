package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import com.reactive.programming.usecase.PaymentService;
import com.reactive.programming.usecase.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  Independent producers
 *      - startWith, concat, merge, zip
 *
 *  Dependent sequential non-blocking IO calls!
 *      - flatMap, flatMapMany
 *      - flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
 *
 */
public class FlatMapOperator {

    public static void main(String[] args) {

        /**
         *  We've username & we need to get user account balance
         *      - if we use map, we will get Mono<Mono<Integer>> & if we subscribe to it, we will get inner mono
         *          so map is good for some kind of in-memory computation.
         *      - if we use flatMap, we will get Mono<Integer>. So, we can easily subscribe to it & get the result.
         *          so flatMap is good for IO requests & cases where we're getting Mono<Mono<T>>.
         *          - Internally, flatMap will subscribe to the inner publisher & emit the result.
         */

        Mono<Mono<Integer>> monoInsideMono = UserService.getUserId("sam")
                .map(PaymentService::getUserBalance);

        monoInsideMono.subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("------------------------------------------------------------------------------");

        Mono<Integer> flatenMono = UserService.getUserId("sam")
                .flatMap(PaymentService::getUserBalance);

        flatenMono.subscribe(DefaultSubscriber.create("sub2"));


        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

}
