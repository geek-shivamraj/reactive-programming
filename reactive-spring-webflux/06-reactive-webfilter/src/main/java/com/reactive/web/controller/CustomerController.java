package com.reactive.web.controller;

import com.reactive.web.dto.CustomerDto;
import com.reactive.web.exceptions.ApplicationException;
import com.reactive.web.service.CustomerService;
import com.reactive.web.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("paginated")
    public Mono<List<CustomerDto>> allCustomers(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "3") Integer size) {
        return customerService.getAllCustomers(page, size).collectList();
    }

    @GetMapping("{id}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(customerService::saveCustomer);
    }

    @PutMapping("{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {

        return mono.transform(RequestValidator.validate())
                .as(validReq -> customerService.updateCustomer(id, validReq))
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then();
    }
}
