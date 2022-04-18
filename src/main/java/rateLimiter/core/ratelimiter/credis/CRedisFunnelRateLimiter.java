package rateLimiter.core.ratelimiter.credis;


import credis.java.client.CacheProvider;
import credis.java.client.transaction.RedisCASAction;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisFunnelProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;
import rateLimiter.utils.RedisClientUtils;

import java.util.List;

public class CRedisFunnelRateLimiter extends AbstractRateLimiter {
    private final Funnel funnel = new Funnel();

    public CRedisFunnelRateLimiter(BasicProperties basicProperties, CRedisFunnelProperties properties) {
        super(basicProperties);
        funnel.key = properties.getStrategyKey();
        long qps = properties.getQps();
        // qps有效值 < 1000
        funnel.interval = 1000/qps;
    }

    @Override
    public boolean limit() throws Exception {
        CacheProvider cacheProvider = RedisClientUtils.getCacheProvider();
        String key = key(funnel.key);
        boolean cas = false;
        boolean[] pass = new boolean[]{false};

        while (!cas) {
            List<Object> ret = cacheProvider.execute((RedisCASAction) (r, watchedValues) -> {
                String lastTickStr = watchedValues[0];
                long cur = System.currentTimeMillis();
                if (lastTickStr == null) {
                    r.setex(key, (int) funnel.interval,Long.toString(cur));
                    pass[0] = true;
                } else {
                    long lastTick = Long.parseLong(lastTickStr);
                    if (cur - lastTick >= funnel.interval ) {
                        r.setex(key, (int) funnel.interval,Long.toString(cur));
                        pass[0] = true;
                    }
                }
            }, key);
            if (ret != null)
                cas = true;
            else
                pass[0] = false;
        }
        return pass[0];
    }

    public String key(String limitKey) {
        return String.format("limitKey_%s",limitKey);
    }

    public static class Funnel {
        private String key;
        private long interval;

        private Funnel() {};
    }
}
