import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class UTFluxTest {

    private Flux<Integer> getItems() {
        return Flux.just(1,2,3)
                .log();
    }

    @Test
    public void fluxTest1() {
        StepVerifier.create(getItems(), 1)
                .expectNext(1)
                .thenCancel()
                .verify();
    }

    @Test
    public void fluxTest2() {
        StepVerifier.create(getItems())
                //.expectNext(1)              // order is important here
                //.expectNext(2)
                //.expectNext(3)
                .expectNext(1, 2, 3)
                .expectComplete()
                .verify();
    }
}
