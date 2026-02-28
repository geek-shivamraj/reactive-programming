package com.reactive.web.validator;

import com.reactive.web.dto.CustomerDto;
import com.reactive.web.exceptions.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationException.missingName())
                .filter(hasValidEmail())
                .switchIfEmpty(ApplicationException.missingValidEmail());
    }

    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDto> hasValidEmail() {
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
    }
}
