package com.rappytv.globaltags.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagCache {

    private static final Map<UUID, PlayerInfo> cache = new HashMap<>();

    public static void add(UUID uuid, PlayerInfo info) {
        cache.put(uuid, info);
    }
    public static boolean has(UUID uuid) {
        return cache.containsKey(uuid);
    }
    public static PlayerInfo get(UUID uuid) {
        return cache.get(uuid);
    }
    public static boolean isEmpty() {
        return cache.isEmpty();
    }
    public static void clear() {
        cache.clear();
    }
}
