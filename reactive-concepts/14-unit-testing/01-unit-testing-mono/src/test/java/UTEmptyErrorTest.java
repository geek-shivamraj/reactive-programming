import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UTEmptyErrorTest {

    Mono<String> getUsername(int userId) {
        return switch (userId) {
          case 1 -> Mono.just("sam");
          case 2 -> Mono.empty();
          default -> Mono.error(new RuntimeException("Invalid input"));
        };
    }

    @Test
    public void userNameOneTest() {
        StepVerifier.create(getUsername(1))
                .expectNext("sam")
                .expectComplete()
                .verify();  // is similar to subscribe
    }

    @Test
    public void emptyTest() {
        StepVerifier.create(getUsername(2))
                .expectComplete()
                .verify();  // is similar to subscribe
    }

    @Test
    public void errorTest1() {
        StepVerifier.create(getUsername(3))
                .expectError()
                .verify();  // is similar to subscribe
    }

    @Test
    public void errorTest2() {
        StepVerifier.create(getUsername(3))
                .expectError(RuntimeException.class)
                .verify();  // is similar to subscribe
    }

    @Test
    public void errorTest3() {
        StepVerifier.create(getUsername(3))
                .expectErrorMessage("Invalid input")
                .verify();  // is similar to subscribe
    }

    @Test
    public void errorTest4() {
        StepVerifier.create(getUsername(3))
                .consumeErrorWith(ex -> {
                    Assertions.assertEquals(RuntimeException.class, ex.getClass());
                    Assertions.assertEquals("Invalid input", ex.getMessage());
                })
                .verify();  // is similar to subscribe
    }
}
