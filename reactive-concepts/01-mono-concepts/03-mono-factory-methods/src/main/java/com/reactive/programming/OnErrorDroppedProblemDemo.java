package com.reactive.programming;

import reactor.core.publisher.Mono;

/**
 *  - We got the ERROR [main] r.core.publisher.Operators: Operator called default onErrorDropped becoz
 *      - Reactor says that you've subscriber to a publisher that don't emit any item but instead gives an error message
 *          & you've provided only data handler & not error handler.
 *
 *  - Solution
 *      - Along with data handler, do provide error handler (even empty is fine too)
 */
public class OnErrorDroppedProblemDemo {

    public static void main(String[] args) {

        System.out.println("------------------------------------------------");
        // This will give ERROR [main] r.core.publisher.Operators: Operator called default onErrorDropped
        getUsername(3).subscribe(
                userName -> System.out.println("userName = " + userName)
        );

        System.out.println("------------------------------------------------");
        // This will execute without the above ERROR
        getUsername(3).subscribe(
                userName -> System.out.println("userName = " + userName),
                error -> {}
        );

        System.out.println("------------------------------------------------");
        getUsername(3).subscribe(
                userName -> System.out.println("userName = " + userName),
                error -> System.out.println("error = " + error)
        );
    }

    private static Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("sam");
            case 2 -> Mono.empty();
            default -> Mono.error(new RuntimeException("Invalid user Id"));
        };
    }
}
