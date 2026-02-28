package com.reactive.web.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 *  - We don't have @Entity in R2DBC
 *  - @Table / @Column are not really required here (coz of same name) but adding it here for our reference.
 */
@Data
@Table("customer")
public class Customer {

    @Id
    private Integer id;

    @Column("name")
    private String name;
    private String email;
}
