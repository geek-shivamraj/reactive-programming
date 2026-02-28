package com.reactive.project.dto;

import com.reactive.project.domain.Ticker;
import com.reactive.project.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity) {
}
