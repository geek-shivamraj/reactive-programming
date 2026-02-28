package com.reactive.programming.assignment.solution;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class GroupByAssignment {

    public static void main(String[] args) {

        orderStream()
                .filter(OrderProcessingService.canProcess())
                .groupBy(PurchaseOrder::category)
                .flatMap(gf -> gf.transform(OrderProcessingService.getProcessor(gf.key())))
                .subscribe(DefaultSubscriber.create("sub1"));

        Util.sleepSeconds(20);
    }

    private static Flux<PurchaseOrder> orderStream() {
        return Flux.interval(Duration.ofMillis(200))
                .map(i -> PurchaseOrder.create());
    }
}
