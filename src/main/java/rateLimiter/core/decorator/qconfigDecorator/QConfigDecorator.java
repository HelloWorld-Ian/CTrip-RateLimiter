package rateLimiter.core.decorator.qconfigDecorator;


import rateLimiter.core.ratelimiter.RateLimiter;

public interface QConfigDecorator extends RateLimiter {
    void init() throws Exception;
}
