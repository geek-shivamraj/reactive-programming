package com.reactive.programming.assignment.solution;

import com.reactive.programming.helper.Util;

public class SlackAssignment {

    public static void main(String[] args) {

        var room = new SlackRoom("Reactor");
        var sam = new SlackMember("sam");
        var bob = new SlackMember("bob");
        var mike = new SlackMember("mike");

        // add 2 members
        room.addMember(sam);
        room.addMember(bob);

        sam.says("Hi all...");
        Util.sleepSeconds(4);

        bob.says("Hey!");
        sam.says("I simply wanted to say hi...");
        Util.sleepSeconds(4);

        room.addMember(mike);
        mike.says("Hey guys... glad to be here...");

    }
}
