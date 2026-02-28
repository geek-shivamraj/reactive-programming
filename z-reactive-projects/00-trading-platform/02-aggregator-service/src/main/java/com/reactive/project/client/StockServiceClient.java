package com.reactive.project.client;

import com.reactive.project.domain.Ticker;
import com.reactive.project.dto.PriceUpdate;
import com.reactive.project.dto.StockPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Slf4j
public class StockServiceClient {

    private final WebClient client;
    private Flux<PriceUpdate> flux;

    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return this.client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    /**
     *  Potential Problem with below approach:
     *      - Let's imagine that we will be creating this flux once, then return this flux again & again.
     *          however there is a good chance that the stock-service might be restarted.
     *      - So, when it's restarted, the connection will be broken & error signal will be emitted and
     *          the flux will not emit any more data right after error.
     *      - Solution: Add retry.
     */
    public Flux<PriceUpdate> priceUpdatesStream() {
        // creating flux only once
        if (Objects.isNull(this.flux)) {
            this.flux = this.getPriceUpdates();
        }
        return this.flux;
    }

    // Hot Publisher with multiple subscribers & keeping this private so others to invoke this method.
    // This publisher should be created only once.
    private Flux<PriceUpdate> getPriceUpdates() {
        return this.client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1);
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> log.error("stock service price stream call failed. retrying: {}", rs.failure().getMessage()));
    }
}
