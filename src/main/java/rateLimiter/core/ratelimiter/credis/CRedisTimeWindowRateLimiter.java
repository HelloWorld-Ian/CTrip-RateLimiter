package rateLimiter.core.ratelimiter.credis;


import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisTimeWindowProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;
import rateLimiter.utils.RedisClientUtils;

public class CRedisTimeWindowRateLimiter extends AbstractRateLimiter {
    private final TimeWindow timeWindow = new TimeWindow();

    public CRedisTimeWindowRateLimiter(BasicProperties basicProperties, CRedisTimeWindowProperties strategy){
        super(basicProperties);
        timeWindow.key = strategy.getStrategyKey();
        timeWindow.time = strategy.getTime()/1000;
        timeWindow.max = strategy.getMax();

        String limitKey = key(timeWindow.key);
        RedisClientUtils.expire(limitKey,0);
    }

    @Override
    public  boolean limit() throws Exception {
        String limitKey = key(timeWindow.key);
        Long cur = RedisClientUtils.incrBy(limitKey, 1);
        if (cur == 1)
            RedisClientUtils.expire(limitKey, timeWindow.time);
        return cur <= timeWindow.max;
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
    public static class TimeWindow{
        private String key;
        private long time;
        private long max;
        private TimeWindow(){}
    }
}
