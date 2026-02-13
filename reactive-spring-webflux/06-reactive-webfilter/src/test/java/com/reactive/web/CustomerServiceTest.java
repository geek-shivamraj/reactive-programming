package com.reactive.web;

import com.reactive.web.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

/**
 *  - Validate HTTP response status codes
 *  - unauthorized - no token
 *  - unauthorized - invalid token
 *  - standard category - GET - success
 *  - standard category - POST/PUT/DELETE - forbidden
 *  - prime category - GET - success
 *  - prime category - POST/PUT/DELETE - success
 */
@Slf4j
@AutoConfigureWebTestClient
public class CustomerServiceTest extends AbstractTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void unauthorized() {

        // no token
        client.get().uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        // invalid token
        validateGet("secret", HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void standardCategory() {
        validateGet("secret123", HttpStatus.OK);
        validatePost("secret123", HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategory() {
        validateGet("secret456", HttpStatus.OK);
        validatePost("secret456", HttpStatus.OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus) {
        client.get().uri("/customers")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        var dto = new CustomerDto(null, "marshal", "marshal@gmail.com");
        client.post().uri("/customers").bodyValue(dto)
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}
