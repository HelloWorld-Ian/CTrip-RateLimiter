package rateLimiter.core.ratelimiter.local;


import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalTimeWindowProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地时间窗限流
 *
 * @author Iancy
 * @date 2022/4/3
 */
public class LocalTimeWindowRateLimiter extends AbstractRateLimiter {
    private final Lock lock = new ReentrantLock();
    private final TimeWindow timeWindow=new TimeWindow();

    public LocalTimeWindowRateLimiter(BasicProperties basicProperties, LocalTimeWindowProperties strategy){
        super(basicProperties);
        timeWindow.active = 0;
        timeWindow.lastTick = System.currentTimeMillis();
        timeWindow.time = strategy.getTime();
        timeWindow.max = strategy.getMax();
    }

    @Override
    public boolean limit() {
        try {
            lock.lock();
            boolean pass=false;
            long cur = System.currentTimeMillis();
            if (cur - timeWindow.lastTick >= timeWindow.time) {
                timeWindow.active=1;
                timeWindow.lastTick=System.currentTimeMillis();
                pass=true;
            } else {
                if (timeWindow.active<timeWindow.max){
                    timeWindow.active+=1;
                    pass=true;
                }
            }
            return pass;
        } finally {
            lock.unlock();
        }
    }

    /**
     * time window
     */
    public static class TimeWindow{
        private long active;
        private long lastTick;
        private long time;
        private long max;
        private TimeWindow(){}
    }
}
