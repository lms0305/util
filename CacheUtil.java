package com.gpdi.plugins.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class CacheUtil {
    private static CacheManager cacheManager;

    public CacheUtil() {
    }

    private static CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = SpringUtil.getBean(CacheManager.class);
        }
        return cacheManager;
    }

    public static Cache getCache(String cacheName) {
        CacheManager cacheManager = getCacheManager();
        Cache cache = cacheManager.getCache(cacheName);
        return cache;
    }

    @Nullable
    public static Object get(String cacheName, String keyPrefix, Object key) {
        if (ObjectUtil.hasEmpty(new Object[]{cacheName, keyPrefix, key})) {
            return null;
        } else {
            Cache.ValueWrapper valueWrapper = getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)));
            return ObjectUtil.isEmpty(valueWrapper) ? null : valueWrapper.get();
        }
    }

    @Nullable
    public static <T> T get(String cacheName, String keyPrefix, Object key, @Nullable Class<T> type) {
        return ObjectUtil.hasEmpty(new Object[]{cacheName, keyPrefix, key}) ? null : getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)), type);
    }

    @Nullable
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        if (ObjectUtil.hasEmpty(new Object[]{cacheName, keyPrefix, key})) {
            return null;
        } else {
            try {
                Cache.ValueWrapper valueWrapper = getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)));
                Object value = null;
                if (valueWrapper == null) {
                    T call = valueLoader.call();
                    if (ObjectUtil.isNotEmpty(call)) {
                        Field field = ReflectUtil.getField(call.getClass(), "id");
                        if (ObjectUtil.isNotEmpty(field) && ObjectUtil.isEmpty(ClassUtil.getDeclaredMethod(call.getClass(), "getId", new Class[0]).invoke(call, new Object[0]))) {
                            return null;
                        }
                        getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), call);
                        value = call;
                    }
                } else {
                    value = valueWrapper.get();
                }
                return (T) value;
            } catch (Exception var9) {
                var9.printStackTrace();
                return null;
            }
        }
    }

    public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
        getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), value);
    }

    public static void evict(String cacheName, String keyPrefix, Object key) {
        if (!ObjectUtil.hasEmpty(new Object[]{cacheName, keyPrefix, key})) {
            getCache(cacheName).evict(keyPrefix.concat(String.valueOf(key)));
        }
    }

    /**
     * 清除缓存
     * @param cacheName
     */
    public static void clear(String cacheName) {
        if (!ObjectUtil.isEmpty(cacheName)) {
            getCache(cacheName).clear();
        }
    }
}