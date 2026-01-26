package com.reactive.programming.assignment.solution;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class WindowingAssignment {

    private static final Logger log = LoggerFactory.getLogger(WindowingAssignment.class);

    public static void main(String[] args) {
        var counter = new AtomicInteger(0);
        var fileNameFormat = "reactive-concepts/10-batching-windowing-grouping/02-op-windowing/src/main/resources/section/file%d.txt";
        validateFolder(Path.of(fileNameFormat));

        eventStream()
                .window(Duration.ofMillis(2000))
                .flatMap(flux -> createFile(flux, Path.of(fileNameFormat.formatted(counter.incrementAndGet()))))
                .subscribe();

        Util.sleepSeconds(20);
    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> "event - " + (i + 1));
    }

    private static Mono<Void> createFile(Flux<String> flux, Path path) {

        var writer = new FileWriter(path);
        return flux.doOnNext(writer::write)
                .doFirst(writer::createFile)
                .doFinally(s -> writer.closeFile())
                .then();
    }

    private static void validateFolder(Path path) {
        var folder = path.getParent();
        try {
            if (Files.exists(folder)) {
                try (Stream<Path> walk = Files.walk(folder)) {
                    walk.sorted(Comparator.reverseOrder())
                            .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            }
            Files.createDirectory(folder);
        } catch (IOException e) {
            log.error("Directory creation failed: {}", folder);
            throw new RuntimeException(e);
        }
        log.info("Directory created: {}", folder);
    }
}
