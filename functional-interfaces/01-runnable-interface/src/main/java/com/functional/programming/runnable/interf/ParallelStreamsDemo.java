package com.functional.programming.runnable.interf;

import com.functional.programming.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 *  - In Java, a Stream is a sequence of elements supporting functional-style operations (like map, filter, forEach).
 *      & by default, stream() runs sequentially on a single thread.
 *  - parallelStream() splits the work across multiple threads from the ForkJoinPool.commonPool (shared pool of threads)
 *  - Because it’s parallel:
 *      - The output order is not guaranteed.
 *      - Multiple log lines may be produced simultaneously by different threads.
 *
 *  Behind the Scenes:
 *      - ForkJoinPool.commonPool: Default pool used by parallel streams.
 *      - Parallelism level: By default, equals the number of available processors (Runtime.getRuntime().availableProcessors()).
 *      - Each element of the list can be processed by a different worker thread.
 *      - The JVM handles splitting the list into chunks and distributing them.
 */
public class ParallelStreamsDemo {

    private static final Logger log = LoggerFactory.getLogger(ParallelStreamsDemo.class);

    public static void main(String[] args) {

        Runnable task = () -> IntStream.range(0, 5).forEach(i -> log.info("{}: {}", i, Util.faker().name().firstName()));

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            //executor.submit(task);
            //executor.submit(task);
            //executor.submit(task);
        }

        int cores = Runtime.getRuntime().availableProcessors();
        log.info("Available cores: {}", cores);

        log.info("********* Sequence Stream Execution *********");
        List<Integer> numbers = IntStream.range(1, 100).boxed().toList();
        numbers.stream().forEach(i -> log.info("{}: {}", i,Util.faker().name().firstName()));

        Util.sleepSeconds(2);
        System.out.println("\n");
        log.info("********* Parallel Stream Execution *********");
        numbers = IntStream.range(100, 200).boxed().toList();
        numbers.parallelStream().forEach(i -> log.info("{}: {}", i,Util.faker().name().firstName()));
    }

}
