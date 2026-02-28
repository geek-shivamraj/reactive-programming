import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UTMonoTest {

    private static final Logger log = LoggerFactory.getLogger(UTMonoTest.class);


    // assume this a method from your service class
    private Mono<String> getProduct(int id) {
        return Mono.fromSupplier(() -> "product-" + id)
                .doFirst(() -> log.info("Invoked"));
    }

    @Test
    public void productOneTest() {
        StepVerifier.create(getProduct(1))
                .expectNext("product-1")
                .expectComplete()
                .verify();  // is similar to subscribe
    }

    @Test
    public void productTwoTest() {
        StepVerifier.create(getProduct(2))
                .expectNext("product-1")
                .expectComplete()
                .verify();  // is similar to subscribe
    }
}
