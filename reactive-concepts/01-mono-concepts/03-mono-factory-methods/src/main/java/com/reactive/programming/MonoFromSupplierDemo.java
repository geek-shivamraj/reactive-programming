package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *  - In Reactive Programming, we've to be lazy as much as possible. Sometimes we might have to delay the execution
 *      & do the work only when it's required otherwise do not do it.
 *
 */
public class MonoFromSupplierDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoFromSupplierDemo.class);

    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4, 5);

        /**
         *  Problem with using Mono.just() below:
         *    - Mono.just(...) is not lazy in the sense that it immediately captures the value you give it.
         *    - For e.g., When you call Mono.just(1), the Mono is created with the value 1 already stored inside.
         *    - That means the Mono doesn’t defer evaluation or wait until subscription time to compute the value — it’s eager.
         *    - On subscription, it simply emits that pre‑captured value and then completes.
         *
         *  Mono.fromSupplier()
         *    - This is lazy in the sense that it doesn’t capture the value you give it.
         *    - For e.g., When you call Mono.fromSupplier(() -> 1), the Mono is created without any value stored inside.
         *    - That means the Mono defers evaluation or wait until subscription time to compute the value — it’s lazy.
         *    - On subscription, it will compute the value & emit it.
         *
         *  Recommendation:
         *    - That’s why Mono.just() is great for wrapping already‑known constants, but if you want deferred execution
         *      (like calling an API, generating a timestamp, or computing something expensive), you’d use fromSupplier or defer.
         */
        Mono.just(sum(list));
               // .subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("--------------------------------------------------------------");

        Mono.fromSupplier(() -> sum(list));
                //.subscribe(DefaultSubscriber.create("sub2"));

        System.out.println("--------------------------------------------------------------");
    }

    private static int sum(List<Integer> list) {
        log.info("Finding the sum of {}", list);
        return list.stream().reduce(0, Integer::sum);
    }
}
