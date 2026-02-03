import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscriber<T> implements Subscriber<T> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
    private final String name;  // Subscriber name

    public DefaultSubscriber(String name) {
        // name will be used in case one publisher is subscriber by multiple subscribers
        this.name = name;
    }

    public static <T> Subscriber<T> create() {
        return new DefaultSubscriber<>("");
    }

    public static <T> Subscriber<T> create(String name) {
        return new DefaultSubscriber<>(name);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T item) {
        log.info("[{}] Received item: {}", name, item);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("[{}] Error: ", name, throwable);
    }

    @Override
    public void onComplete() {
        log.info("[{}] Completed", name);
    }
}
