package rateLimiter.core.decorator.qconfigDecorator.credis;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisTimeWindowProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.credis.CRedisTimeWindowRateLimiter;

public class CRedisTimeWindowDecorator implements QConfigDecorator {
    private CRedisTimeWindowRateLimiter cRedisTimeWindowRateLimiter;
    private BasicProperties basicProperties;
    private CRedisTimeWindowProperties cRedisTimeWindowProperties;

    public CRedisTimeWindowDecorator(JsonNode node) {
        this.setCRedisTimeWindowProperties(node);
        this.setBasicProperties(node);
        this.init();
    }

    public void setCRedisTimeWindowProperties(JsonNode node) {
        cRedisTimeWindowProperties = new CRedisTimeWindowProperties();
        cRedisTimeWindowProperties.setTime(Long.parseLong(node.get("time").asText()));
        cRedisTimeWindowProperties.setMax(Long.parseLong(node.get("max").asText()));
        cRedisTimeWindowProperties.setStrategyKey(node.get("strategyKey").asText());
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        cRedisTimeWindowRateLimiter = new CRedisTimeWindowRateLimiter(basicProperties, cRedisTimeWindowProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return cRedisTimeWindowRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return cRedisTimeWindowRateLimiter.doRateLimit();
    }
}
