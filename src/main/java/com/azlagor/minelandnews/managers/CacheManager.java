package com.azlagor.minelandnews.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CacheManager {
    private static final Cache<Integer, String> apiCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public static void putCache(int id, String text)
    {
        apiCache.put(id,text);
    }
    public static void clear()
    {
        apiCache.cleanUp();
    }
    public static List<String> getNRecordsFromCache(int n) {
        List<String> allRecords = apiCache.asMap()
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey())) // Сортировка по ID (от большего к меньшему)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        if (n >= allRecords.size()) {
            return allRecords;
        } else {
            return allRecords.subList(0, n);
        }
    }
    public static Integer getKeyByValue(String value) {
        for (Map.Entry<Integer, String> entry : apiCache.asMap().entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
