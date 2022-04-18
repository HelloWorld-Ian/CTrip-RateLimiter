package rateLimiter.core.decorator.qconfigDecorator.credis;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisRollingWindowProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.credis.CRedisRollingWindowRateLimiter;

public class CRedisRollingWindowDecorator implements QConfigDecorator {
    private CRedisRollingWindowRateLimiter rateLimiter;
    private BasicProperties basicProperties;
    private CRedisRollingWindowProperties cRedisRollingWindowProperties;

    public CRedisRollingWindowDecorator(JsonNode node) {
        this.setBasicProperties(node);
        this.setCRedisRollingWindowProperties(node);
        this.init();
    }

    public void setCRedisRollingWindowProperties(JsonNode node) {
        cRedisRollingWindowProperties = new CRedisRollingWindowProperties();
        cRedisRollingWindowProperties.setMax(Long.parseLong(node.get("max").asText()));
        cRedisRollingWindowProperties.setTime(Long.parseLong(node.get("time").asText()));
        cRedisRollingWindowProperties.setStrategyKey(node.get("strategyKey").asText());
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        rateLimiter = new CRedisRollingWindowRateLimiter(basicProperties, cRedisRollingWindowProperties);
    }

    @Override
    public boolean limit() throws Exception {
        return rateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return rateLimiter.doRateLimit();
    }
}
