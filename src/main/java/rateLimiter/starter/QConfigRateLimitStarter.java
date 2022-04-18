package rateLimiter.starter;


import rateLimiter.core.factory.QConfigRateLimitFactory;
import rateLimiter.core.ratelimiter.RateLimiter;
import rateLimiter.exception.RateLimitException;

/**
 * 启动限流的限流入口
 *
 * @author Iancy
 * @date 2022/4/5
 */
public class QConfigRateLimitStarter {
    public static boolean doRateLimit(String strategy) throws Exception {
       RateLimiter rateLimiter = QConfigRateLimitFactory.getInstance(strategy);
       if (rateLimiter != null) {
           return rateLimiter.doRateLimit();
       } else {
           throw new RateLimitException("strategy not found");
       }
    }
}
