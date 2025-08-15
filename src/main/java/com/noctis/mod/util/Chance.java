package com.noctis.mod.util;

import java.util.concurrent.ThreadLocalRandom;

public final class Chance {
    private Chance() {}
    public static boolean roll(double probability) {
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
