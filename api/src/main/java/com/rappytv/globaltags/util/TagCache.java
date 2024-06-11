package com.rappytv.globaltags.util;

import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.types.PlayerInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class TagCache {

    private TagCache() {}

    private static final Map<UUID, PlayerInfo> cache = new HashMap<>();
    private static final Set<UUID> resolving = new HashSet<>();

    public static void add(UUID uuid, PlayerInfo info) {
        cache.put(uuid, info);
    }
    public static void remove(UUID uuid) {
        cache.remove(uuid);
    }
    public static boolean has(UUID uuid) {
        return cache.containsKey(uuid);
    }
    public static PlayerInfo get(UUID uuid) {
        return cache.get(uuid);
    }
    public static void resolve(UUID uuid) {
        resolve(uuid, (info) -> {});
    }
    public static void resolve(UUID uuid, Consumer<PlayerInfo> consumer) {
        if(has(uuid)) {
            consumer.accept(get(uuid));
            return;
        }
        if(resolving.contains(uuid)) return;
        resolving.add(uuid);
        ApiHandler.getInfo(uuid, (info) -> {
            add(uuid, info);
            resolving.remove(uuid);
            resolve(uuid, consumer);
        });
    }
    public static void clear() {
        cache.clear();
    }
}
