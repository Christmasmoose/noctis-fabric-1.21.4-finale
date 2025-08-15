package com.noctis.mod.util;

import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public final class Cooldowns {
    private static final Map<String, Long> COOLDOWNS = new HashMap<>();
    private Cooldowns() {}
    private static String key(Entity e, String name) { return e.getUuidAsString() + ":" + name; }

    public static boolean ready(Entity e, String name, long ms) {
        long now = System.currentTimeMillis();
        String k = key(e, name);
        Long until = COOLDOWNS.get(k);
        if (until == null || now >= until) {
            COOLDOWNS.put(k, now + ms);
            return true;
        }
        return false;
    }

    public static void set(Entity e, String name, long ms) {
        COOLDOWNS.put(key(e, name), System.currentTimeMillis() + ms);
    }
}
