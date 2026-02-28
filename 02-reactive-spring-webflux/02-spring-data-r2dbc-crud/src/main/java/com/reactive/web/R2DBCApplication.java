package com.reactive.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "com.reactive.web")
@EnableR2dbcRepositories(basePackages = "com.reactive.web")
public class R2DBCApplication {

    public static void main(String[] args) {

    }
}
