package rateLimiter.core.ratelimiter;

import lombok.Setter;
import rateLimiter.config.BasicProperties;
import rateLimiter.log.RateLimitLogger;

/**
 * @author Iancy
 * @date 2022/4/3
 */
@SuppressWarnings("all")
@Setter
public abstract class AbstractRateLimiter implements RateLimiter {

    BasicProperties basicProperties;

    public AbstractRateLimiter(BasicProperties basicProperties) {
        this.basicProperties = basicProperties;
    }

    /**
     * the external method to do rate limit
     */
    public boolean doRateLimit() throws Exception {
        long waitTime = basicProperties.getTimeout();
        long timeout = waitTime + System.currentTimeMillis();
        boolean limitRetry = basicProperties.isLimitRetry();
        long retryPeriod = basicProperties.getRetryPeriod();
        boolean pass = false;
        while (!pass) {
            if (limit()) {
                pass = true;
            } else if (!limitRetry) {
                RateLimitLogger.infoLog("retry policy is forbidden, request is rejected");
                break;
            } else {
                long curTime = System.currentTimeMillis();
                if(curTime > timeout){
                    RateLimitLogger.infoLog("time out, request is rejected");
                    break;
                }
                Thread.sleep(retryPeriod);
            }
        }
        return pass;
    }
}
