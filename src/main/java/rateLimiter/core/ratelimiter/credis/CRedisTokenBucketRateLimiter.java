package rateLimiter.core.ratelimiter.credis;

import credis.java.client.CacheProvider;
import credis.java.client.transaction.RedisCASAction;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisTokenBucketProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;
import rateLimiter.utils.RedisClientUtils;

import java.util.List;

public class CRedisTokenBucketRateLimiter extends AbstractRateLimiter {
    private final Bucket bucket = new Bucket();

    public CRedisTokenBucketRateLimiter(BasicProperties basicProperties, CRedisTokenBucketProperties strategy){
        super(basicProperties);
        bucket.key = strategy.getStrategyKey();
        bucket.capacity = strategy.getCapacity();
        bucket.interval = strategy.getInterval();
        bucket.quantum = strategy.getQuantum();
    }

    @Override
    public boolean limit() throws Exception {
        CacheProvider cacheProvider = RedisClientUtils.getCacheProvider();
        String bucketKey = key(bucket.key);
        int expire = expire(bucket.quantum, bucket.capacity, bucket.interval);
        final Boolean[] pass = {false};
        boolean cas = false;

        while (!cas) {
            List<Object> ret = cacheProvider.execute((RedisCASAction) (r, values) -> {
                String val = values[0];
                if (val == null) {
                    r.set(bucketKey,assemble_val(bucket.capacity-1, System.currentTimeMillis()));
                    pass[0] = true;
                } else {
                    long[]arr = split_val(val);
                    long tokens = arr[0];
                    long lastTimePoint = arr[1];
                    long totalAdd = increment(lastTimePoint, bucket.quantum, bucket.interval);
                    if (tokens > 0||totalAdd > 0) {
                        String newVal;
                        if(totalAdd > 0)
                            newVal = assemble_val(Math.min(bucket.capacity, tokens + totalAdd) - 1, System.currentTimeMillis());
                        else
                            newVal = assemble_val(tokens - 1, lastTimePoint);
                        r.setex(bucketKey, expire, newVal);
                        pass[0] = true;
                    }
                    if(tokens < 0 || totalAdd < 0)
                        r.set(bucketKey,assemble_val(0,System.currentTimeMillis()));
                }
            }, bucketKey);

            if(ret != null)
                cas = true;
            else
                pass[0] = false;
        }
        return pass[0];
    }

    /**
     * the total tokens add to bucket during the last tick to current
     */
    private long increment(long lastTick,long quantum,long interval){
        long cur = System.currentTimeMillis();
        long round = (cur - lastTick)/ interval;
        return round * quantum;
    }

    /**
     * 获取redis中的存储key
     */
    public static String key(String limitKey){
        return String.format("limitKey_%s",limitKey);
    }

    /**
     * 合成 val
     */
    public static String assemble_val(long count,long time){
        return count+"_"+time;
    }

    /**
     * 分解 val
     */
    public static long[] split_val(String val){
        String[]splits=val.split("_");
        return new long[]{Long.parseLong(splits[0]),Long.parseLong(splits[1])};
    }

    /**
     * expire time
     */
    public static int expire(long addNum,long capacity,long interval){
        return (int) (Math.ceil((double)capacity/addNum)*interval)/1000;
    }

    /**
     * the bucket which stores the token
     */
    public static class Bucket{
        private String key;
        private long interval;
        private long capacity;
        private long quantum;

        private Bucket(){}
    }
}
