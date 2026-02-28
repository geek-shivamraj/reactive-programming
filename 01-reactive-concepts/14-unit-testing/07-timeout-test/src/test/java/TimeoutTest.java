import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.util.function.UnaryOperator;

public class TimeoutTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200));
    }

    @Test
    public void timeoutTest() {
        StepVerifier.create(getItems())
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                //.verify(Duration.ofMillis(500));
                .verify(Duration.ofMillis(1500));
    }
}
