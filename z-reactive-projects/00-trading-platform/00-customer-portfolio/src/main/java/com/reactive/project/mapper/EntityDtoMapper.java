package com.reactive.project.mapper;

import com.reactive.project.domain.Ticker;
import com.reactive.project.dto.CustomerInformation;
import com.reactive.project.dto.Holding;
import com.reactive.project.dto.StockTradeRequest;
import com.reactive.project.dto.StockTradeResponse;
import com.reactive.project.entity.Customer;
import com.reactive.project.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items) {

        var holdings = items.stream()
                .map(item -> new Holding(item.getTicker(), item.getQuantity()))
                .toList();
        return new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holdings);
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        var portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customerId);
        portfolioItem.setTicker(ticker);
        portfolioItem.setQuantity(0);
        return portfolioItem;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerId, Integer balance) {
        return new StockTradeResponse(customerId, request.ticker(), request.price(), request.quantity(), request.action(),
                request.totalPrice(), balance);
    }
}
