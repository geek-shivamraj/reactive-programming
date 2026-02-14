package com.reactive.web.service;

import com.reactive.web.dto.ProductDto;
import com.reactive.web.mapper.EntityDtoMapper;
import com.reactive.web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> saveProducts(Flux<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .as(repository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductsCount() {
        return repository.count();
    }

    public Flux<ProductDto> allProducts() {
        return repository.findAll().map(EntityDtoMapper::toDto);
    }
}
