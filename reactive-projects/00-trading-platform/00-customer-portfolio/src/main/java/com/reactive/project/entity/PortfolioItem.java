package com.reactive.project.entity;

import com.reactive.project.domain.Ticker;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PortfolioItem {

    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;
}
