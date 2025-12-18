package com.reactive.programming.mono.flux.conversion;

import com.reactive.programming.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  - We can use Flux.from() to convert Mono to Flux
 */
public class MonoFluxConversion {

    private static final Logger log = LoggerFactory.getLogger(MonoFluxConversion.class);

    public static void main(String[] args) {

         monoToFlux();

         log.info("------------------------------------------");

        // Since Mono can emit only one message, so we can use "next()" method or Mono.from() to emit the first message.
        var flux = Flux.range(1, 10);
        flux.next().subscribe(DefaultSubscriber.create("sub2"));

        log.info("------------------------------------------");

        Mono.from(flux).subscribe(DefaultSubscriber.create("sub3"));
    }

    private static void monoToFlux() {
        var username = getUsername(1);
        save(Flux.from(username));
    }

    private static Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("sam");
            case 2 -> Mono.empty();
            default -> Mono.error(new RuntimeException("Invalid user id"));
        };
    }

    private static void save(Flux<String> flux) {
        flux.subscribe(DefaultSubscriber.create("sub1"));
    }
}
