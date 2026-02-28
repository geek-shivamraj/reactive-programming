package com.reactive.programming.client.exceptions;

public class ClientError extends RuntimeException {

    public ClientError() {
        super("Bad Request");
    }

}
