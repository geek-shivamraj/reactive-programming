package com.reactive.web.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Customer {

    @Id
    private Integer id;
    private String name;
    private String email;
}
