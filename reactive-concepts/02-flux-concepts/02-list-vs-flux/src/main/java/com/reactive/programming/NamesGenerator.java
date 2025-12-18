package com.reactive.programming;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

public class NamesGenerator {

    private static final Logger log = LoggerFactory.getLogger(NamesGenerator.class);

    public static List<String> generateNamesList(int count) {
        log.info("Generating names using List...");
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> generateName())
                .toList();
    }

    public static Flux<String> generateNamesFlux(int count) {
        log.info("Generating names using Flux...");
        return Flux.range(1, count)
                .map(i -> generateName());
    }

    private static String generateName() {
        Util.sleepSeconds(1);
        return Faker.instance().name().firstName();
    }
}

