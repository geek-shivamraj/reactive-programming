package com.reactive.project.dto;

import com.reactive.project.domain.Ticker;
import com.reactive.project.domain.TradeAction;

public record StockTradeRequest(Ticker ticker, Integer price,
                                Integer quantity, TradeAction action) {

    public Integer totalPrice() {
        return price * quantity;
    }
}
