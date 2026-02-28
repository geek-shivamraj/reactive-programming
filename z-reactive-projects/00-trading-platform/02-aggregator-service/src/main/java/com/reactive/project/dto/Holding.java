package com.reactive.project.dto;

import com.reactive.project.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
