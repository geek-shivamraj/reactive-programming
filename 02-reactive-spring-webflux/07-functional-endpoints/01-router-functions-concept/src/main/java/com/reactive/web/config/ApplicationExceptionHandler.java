package com.reactive.web.config;

import com.reactive.web.exceptions.CustomerNotFoundException;
import com.reactive.web.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Service
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request) {
//        var status = HttpStatus.NOT_FOUND;
//        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
//        problem.setType(URI.create("https://example.com/problem/customer-not-found"));
//        problem.setTitle("Customer Not Found");
//        problem.setInstance(URI.create(request.path()));
//        return ServerResponse.status(status).bodyValue(problem);

        return handleException(HttpStatus.NOT_FOUND, ex, request, problem -> {
            problem.setType(URI.create("https://example.com/problem/customer-not-found"));
            problem.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InvalidInputException.class)
    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request) {
//        var status = HttpStatus.BAD_REQUEST;
//        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
//        problem.setType(URI.create("https://example.com/problem/invalid-input"));
//        problem.setTitle("Invalid Input");
//        problem.setInstance(URI.create(request.path()));
//        return ServerResponse.status(status).bodyValue(problem);

        return handleException(HttpStatus.BAD_REQUEST, ex, request, problem -> {
            problem.setType(URI.create("https://example.com/problem/invalid-input"));
            problem.setTitle("Invalid Input");
        });
    }

    private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, ServerRequest request, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setInstance(URI.create(request.path()));
        consumer.accept(problem);
        return ServerResponse.status(status).bodyValue(problem);
    }
}
