package com.reactive.web.config;

import com.reactive.web.exceptions.CustomerNotFoundException;
import com.reactive.web.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 *  3 ways to organize the router beans:
 *      Way 1: Create 1 router bean & add all the paths & exception handling logic
 *      Way 2: Create multiple router beans based on some standard & then add exception logic for each bean
 *      Way 3: Use .path() & route the logic based on the path to child router. Exception handling logic will be reused.
 */

@Configuration
public class RouterConfiguration {

    @Autowired
    private CustomerRequestHandler customerRequestHandler;

    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        // Be a bit more cautious while writing the APIs in order.
        return RouterFunctions.route()
                // Way 3:
                //.path("customers", this::customerRelatedRoutes)
                .GET("/customers", customerRequestHandler::allCustomers)
                .GET("/customers/paginated", customerRequestHandler::paginatedCustomers)
                .GET("/customers/{id}", customerRequestHandler::getCustomer)
                .POST("/customers", customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                // Web filters
//                .filter((request, next) -> {
//                    return ServerResponse.badRequest().build();
//                })
//                .filter((request, next) -> {
//                    return ServerResponse.badRequest().build();
//                })
                .build();
    }

    // We can create multiple router functions
    // Be cautious with exception handling
    // Way 2
    //@Bean
    public RouterFunction<ServerResponse> customerGetRoutes() {
        // Be a bit more cautious while writing the APIs in order.
        return RouterFunctions.route()
                .GET("/customers", customerRequestHandler::allCustomers)
                .GET("/customers/paginated", customerRequestHandler::paginatedCustomers)
                .GET("/customers/{id}", customerRequestHandler::getCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }

    // way 3
    private RouterFunction<ServerResponse> customerRelatedRoutes() {
        // Be a bit more cautious while writing the APIs in order.
        return RouterFunctions.route()
                .GET("/paginated", customerRequestHandler::paginatedCustomers)
                .GET("/{id}", customerRequestHandler::getCustomer)
                .GET(customerRequestHandler::allCustomers)
                .build();
    }

}
