import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class ScenarioNameTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 3);
    }

    @Test
    public void scenarioNameTest() {
        var options = StepVerifierOptions.create().scenarioName("1 to 3 items test");
        StepVerifier.create(getItems(), options)
                .expectNext(1, 2)
                .expectComplete()
                .verify();
    }

    @Test
    public void stepDescriptionTest() {
        var options = StepVerifierOptions.create().scenarioName("1 to 3 items test");
        StepVerifier.create(getItems(), options)
                .expectNext(11)
                .as("First item should be 11")
                .expectNext(2, 3)
                .as("then 2 & 3")
                .expectComplete()
                .verify();
    }
}
