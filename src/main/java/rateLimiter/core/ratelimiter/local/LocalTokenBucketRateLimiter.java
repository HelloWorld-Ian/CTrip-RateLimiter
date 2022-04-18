package rateLimiter.core.ratelimiter.local;

import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalTokenBucketProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;

public class LocalTokenBucketRateLimiter extends AbstractRateLimiter {
    private final Bucket bucket=new Bucket();

    public LocalTokenBucketRateLimiter(BasicProperties basicProperties, LocalTokenBucketProperties strategy){
        super(basicProperties);
        bucket.interval = strategy.getInterval();
        bucket.capacity = strategy.getCapacity();
        bucket.quantum = strategy.getQuantum();

        bucket.lastTick = System.currentTimeMillis();
        bucket.tokens = bucket.capacity;
    }

    @Override
    public synchronized boolean limit() {
        long increment =increment(bucket);
        long curToken = bucket.tokens;

        if (curToken > 0 || increment > 0) {
            if(increment > 0) {
                bucket.tokens = Math.min(bucket.capacity, curToken+increment) - 1;
                bucket.lastTick = System.currentTimeMillis();
            } else
                bucket.tokens -= 1;
            return true;
        }
        return false;
    }

    /**
     * the total tokens add to bucket during the last tick to current
     */
    private long increment(Bucket bucket){
        long cur = System.currentTimeMillis();
        long round = (cur - bucket.lastTick)/ bucket.interval;
        return round * bucket.quantum;
    }

    public static class Bucket{
        private long tokens;
        private long interval;
        private long lastTick;
        private long capacity;
        private long quantum;
        private Bucket(){}
    }
}
