package com.functional.programming;

import com.github.javafaker.Faker;

public class Util {

    private static final Faker faker = Faker.instance();

    public static Faker faker() {
        return faker;
    }

    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
