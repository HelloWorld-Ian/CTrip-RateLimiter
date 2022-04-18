package rateLimiter.core.ratelimiter.local;

import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalFunnelProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;

;

public class LocalFunnelRateLimiter extends AbstractRateLimiter {
    private final Funnel funnel = new Funnel();

    public LocalFunnelRateLimiter(BasicProperties basicProperties, LocalFunnelProperties properties) {
        super(basicProperties);
        long qps = properties.getQps();
        // qps有效值 < 1000
        funnel.interval = 1000/qps;
        funnel.lastTick = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean limit() throws Exception {
        long cur = System.currentTimeMillis();
        if (cur - funnel.lastTick < funnel.interval)
            return false;
        else {
            funnel.lastTick = cur;
            return true;
        }
    }

    public static class Funnel {
        private long lastTick;
        private long interval;

        private Funnel() {};
    }
}
