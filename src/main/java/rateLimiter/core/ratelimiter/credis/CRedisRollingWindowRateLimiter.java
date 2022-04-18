package rateLimiter.core.ratelimiter.credis;

import credis.java.client.CacheProvider;
import credis.java.client.pipeline.CachePipeline;
import lombok.Setter;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisRollingWindowProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;
import rateLimiter.utils.RedisClientUtils;

public class CRedisRollingWindowRateLimiter extends AbstractRateLimiter {
    private final RollingWindow rollingWindow = new RollingWindow();

    public CRedisRollingWindowRateLimiter(BasicProperties basicProperties, CRedisRollingWindowProperties properties) {
        super(basicProperties);
        rollingWindow.setKey(properties.getStrategyKey());
        rollingWindow.setTime(properties.getTime());
        rollingWindow.setMax(properties.getMax());
    }

    @Override
    public boolean limit() throws Exception {
        CacheProvider cacheProvider = RedisClientUtils.getCacheProvider();
        String key = key(rollingWindow.key);
        CachePipeline pipeline = cacheProvider.getPipeline();
        long cur = System.currentTimeMillis();
        cacheProvider.zremrangeByScore(key,0,cur - rollingWindow.time);
        cacheProvider.zadd(key, cur, Long.toString(cur));
        long count = cacheProvider.zcard(key);
        cacheProvider.expire(key, rollingWindow.time);
        pipeline.sync();
        return count < rollingWindow.max;
    }

    /**
     * 获取redis中的存储key
     */
    public static String key(String limitKey){
        return String.format("limitKey_%s",limitKey);
    }

    /**
     * time window
     */
    @Setter
    public static class RollingWindow {
        private String key;
        private long time;
        private long max;
        private RollingWindow(){}
    }
}
