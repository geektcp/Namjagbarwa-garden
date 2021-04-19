package com.geektcp.garden.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by chengmo on 2017/12/27.
 */
public class CacheFactory {

    private static final int TIME_OUT_MINUTES = 10;

    public static <K, V> LoadingCache<K, V> create(CacheLoader<K, V> loader) {
        return create(loader, TIME_OUT_MINUTES);
    }

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader, long durationMins) {
        return CacheBuilder.newBuilder()
                .refreshAfterWrite(durationMins, TimeUnit.MINUTES)
                .build(loader);
    }

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader, RemovalListener removalListener,
                                                   long durationMins) {
        return CacheBuilder.newBuilder()
                .refreshAfterWrite(durationMins, TimeUnit.MINUTES)
                .removalListener(removalListener)
                .build(loader);
    }
}
