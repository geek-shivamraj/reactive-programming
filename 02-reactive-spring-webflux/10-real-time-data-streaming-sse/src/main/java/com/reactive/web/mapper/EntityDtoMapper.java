package com.reactive.web.mapper;

import com.reactive.web.dto.ProductDto;
import com.reactive.web.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto) {
        var product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDto toDto(Product entity) {
        return new ProductDto(entity.getId(), entity.getDescription(), entity.getPrice());
    }
}
