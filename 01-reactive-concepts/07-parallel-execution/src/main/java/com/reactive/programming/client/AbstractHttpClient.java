package com.reactive.programming.client;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

/**
 *  Creating this Abstract class using Netty to send an HTTP request.
 *      - Later on, this class will be extended to create a client for external services.
 */
public abstract class AbstractHttpClient {

    public static final String BASE_URL = "http://localhost:7070";

    protected final HttpClient httpClient;

    /**
     *  - By default, Reactor netty creates one thread per CPU core. So, if your CPU has 10 cores, it will create 10 threads
     *      but here we want to create only one thread & see how one thread can do all the work.
     *  - One Thread can do a lot of work than we could imagine.
     *
     *  - runOn() method accepts EventLoopGroup & LoopResources. What's the diff b/w them?
     *      - We can image loop resources is basically kind of helper/manager to create the EventLoopGroup & behind the scene
     *          it creates the EventLoopGroup.
     */
    public AbstractHttpClient() {
        var loopResources = LoopResources.create("batman", 1, true);
        this.httpClient = HttpClient.create().runOn(loopResources).baseUrl(BASE_URL);
    }
}
