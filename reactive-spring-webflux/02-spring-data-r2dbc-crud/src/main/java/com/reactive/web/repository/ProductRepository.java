package com.reactive.web.repository;

import com.reactive.web.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(int from, int to);

    // findBy is same as findAllBy
    Flux<Product> findBy(Pageable pageable);
}
