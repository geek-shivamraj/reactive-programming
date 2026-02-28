package com.reactive.project.dto;

import com.reactive.project.domain.Ticker;
import com.reactive.project.domain.TradeAction;

public record StockTradeResponse(Integer customerId, Ticker ticker, Integer price, Integer quantity,
                                 TradeAction action, Integer totalPrice, Integer balance) {
}
