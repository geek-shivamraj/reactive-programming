package com.reactive.web.service;

import com.reactive.web.dto.ProductDto;
import com.reactive.web.mapper.EntityDtoMapper;
import com.reactive.web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Sinks.Many<ProductDto> sink;

    public Mono<ProductDto> saveProducts(Mono<ProductDto> mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(repository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(dto -> sink.tryEmitNext(dto));
    }

    public Flux<ProductDto> productStream() {
        return sink.asFlux();
    }
}
