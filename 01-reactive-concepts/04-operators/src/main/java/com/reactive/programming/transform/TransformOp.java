package com.reactive.programming.transform;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 *  - Transform Operators helps us to write reusable code in reactive pipeline.
 *      - In Traditional/OOPs, we consider everything as object & they talk to each other.
 *      - In Reactive programming, all the complex business workflows are built as a Publisher & Subscriber through
 *          which data will flow from one end to another.
 *   - For e.g., Suppose you've 3 flow for order
 *      - Create Order, Update Order, Cancel Order
 *      - All these flows are built as Publisher & Subscriber pipeline & often time we will have some redundant steps/operators in each flow.
 *      - Using "transform" operator, we can reuse the common steps/operators across all the reactive pipelines.
 *          - We can build the common steps/operators separately & then use/include it wherever required.
 *
 *   - Using "transform", we can create our own Custom Operator.
 */
public class TransformOp {

    private static final Logger log = LoggerFactory.getLogger(TransformOp.class);

    record Customer(int id, String name){}
    record PurchaseOrder(String productName, int price, int quantity){}

    public static void main(String[] args) {

        // Make this false, in case we don't want to print log.
        var isDebugEnable = true;
        getCustomers().transform(isDebugEnable ? addDebugger() : Function.identity())
                //.doOnNext(customer -> log.info("Received: {}", customer))
                //.doOnComplete(() -> log.info("Fetch Completed!"))
                //.doOnError(e -> log.error("Error", e))
                .subscribe();
        getPurchaseOrders().transform(addDebugger())
                //.doOnNext(customer -> log.info("Received: {}", customer))
                //.doOnComplete(() -> log.info("Fetch Completed!"))
                //.doOnError(e -> log.error("Error", e))
                .subscribe();
    }

    // Since Function<Flux<T>, Flux<T>> is a Function, we can use UnaryOperator
    private static <T> UnaryOperator<Flux<T>> addDebugger() {
        return flux -> flux
                .doOnNext(customer -> log.info("Received: {}", customer))
                .doOnComplete(() -> log.info("Fetch Completed!"))
                .doOnError(ex -> log.error("Error", ex));
    }

    private static Flux<Customer> getCustomers() {
        return Flux.range(1, 3)
                .map(i -> new Customer(i, Util.faker().name().firstName()));
    }

    private static Flux<PurchaseOrder> getPurchaseOrders() {
        return Flux.range(1, 5)
                .map(i -> new PurchaseOrder(Util.faker().commerce().productName(), i, i*10));
    }
}
