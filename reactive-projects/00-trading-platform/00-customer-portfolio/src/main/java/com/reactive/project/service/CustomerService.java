package com.reactive.project.service;

import com.reactive.project.dto.CustomerInformation;
import com.reactive.project.entity.Customer;
import com.reactive.project.exceptions.ApplicationException;
import com.reactive.project.mapper.EntityDtoMapper;
import com.reactive.project.repository.CustomerRepository;
import com.reactive.project.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerRepository.findById(customerId)
                .doOnNext(customer -> log.info("Customer {} found", customer))
                .switchIfEmpty(ApplicationException.customerNotFound(customerId))
                .flatMap(this::buildCustomerInformation);
    }

    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        return portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .doOnNext(items -> log.info("Portfolio items {} found", items))
                .map(items -> EntityDtoMapper.toCustomerInformation(customer, items));
    }
}
