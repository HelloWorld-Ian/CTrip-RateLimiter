package rateLimiter.core.ratelimiter;

public interface RateLimiter {
    /**
     * 限流策略
     * @return return true：通过限流策略，return false：被限流策略拒绝
     */
    boolean limit() throws Exception;

    /**
     * 定义限流过程
     * @return return true：通过限流，return false：限流拒绝
     */
    boolean doRateLimit() throws Exception;
}
