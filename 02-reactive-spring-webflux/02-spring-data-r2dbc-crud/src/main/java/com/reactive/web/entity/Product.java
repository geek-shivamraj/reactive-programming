package com.reactive.web.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("Product")
public class Product {

    @Id
    private Integer id;
    private String description;
    private Integer price;
}
