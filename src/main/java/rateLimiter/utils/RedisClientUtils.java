package rateLimiter.utils;

import credis.java.client.CacheProvider;

/**
 * redis client, 用于操作限流需要的缓存数据
 */
public class RedisClientUtils {
    private static CacheProvider cacheProvider = null;

    public static void setCacheProvider(CacheProvider cacheProvider) {
        RedisClientUtils.cacheProvider = cacheProvider;
    }

    public static CacheProvider getCacheProvider(){
        return cacheProvider;
    }

    public static Long incrBy(String key, long increment) {
        return cacheProvider.incrBy(key, increment);
    }

    public static void expire(String key, long ttl) {
        cacheProvider.expire(key, ttl);
    }

    public static Long ttl(String key) {
        return cacheProvider.ttl(key);
    }

}
