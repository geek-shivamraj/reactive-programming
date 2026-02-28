package com.reactive.programming.assignment.solution;

import com.reactive.programming.helper.Util;

public record PurchaseOrder (String item, String category, Integer price) {

    public static PurchaseOrder create() {
        var commerce = Util.faker().commerce();
        return new PurchaseOrder(commerce.productName(),
                commerce.department(), Util.faker().random().nextInt(10, 100));
    }
}
