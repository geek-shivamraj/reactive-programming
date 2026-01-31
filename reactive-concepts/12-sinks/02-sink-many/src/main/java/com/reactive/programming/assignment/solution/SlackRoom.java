package com.reactive.programming.assignment.solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SlackRoom {

    private static final Logger log = LoggerFactory.getLogger(SlackRoom.class);

    private final String name;
    private final Sinks.Many<SlackMessage> sink;
    private final Flux<SlackMessage> flux;

    public SlackRoom(String name) {
        this.name = name;
        this.sink = Sinks.many().replay().all();
        this.flux = sink.asFlux();
    }

    public void addMember(SlackMember slackMember) {
        log.info("{} joined the room {}", slackMember.getName(), name);
        subscribeToRoomMessage(slackMember);
        slackMember.setMessageConsumer(msg -> postMessage(slackMember.getName(), msg));
    }

    private void subscribeToRoomMessage(SlackMember slackMember) {
        this.flux
                // Filtering messages for sender != receiver
                .filter(sm -> !sm.sender().equalsIgnoreCase(slackMember.getName()))
                .map(sm -> sm.formatForDelivery(slackMember.getName()))
                .subscribe(slackMember::receives);
    }

    private void postMessage(String sender, String message) {
        this.sink.tryEmitNext(new SlackMessage(sender, message));
    }
}
