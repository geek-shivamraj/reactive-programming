package com.reactive.programming;

import com.reactive.programming.publisher.PublisherImpl;
import com.reactive.programming.subscriber.SubscriberImpl;

/**
 *      1. Publisher doesn't produce data unless Subscriber requests for it.
 *      2. Publisher will produce only <= subscriber requested items. Publisher can also produce 0 items.
 *      3. Subscriber can cancel the subscription. Producer should stop at that moment as subscriber is no longer
 *          interested in consuming the data.
 *      4. Producer can send the error signal to indicate something is wrong.
 */
public class Sec01AppDemo {

    public static void main(String[] args) throws InterruptedException {
        //case1();    // no items are published as the subscriber hasn't requested items in case 1.

        //case2();

        //case3();

        case4();
    }

    private static void case4() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);

        subscriber.getSubscription().request(11);
        Thread.sleep(2000);

        subscriber.getSubscription().request(4);
        Thread.sleep(2000);
    }

    private static void case3() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);

        subscriber.getSubscription().cancel();

        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
    }

    private static void case2() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
    }

    private static void case1() {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
    }
}


