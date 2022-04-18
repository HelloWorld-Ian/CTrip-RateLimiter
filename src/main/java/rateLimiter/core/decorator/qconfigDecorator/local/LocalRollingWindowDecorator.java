package rateLimiter.core.decorator.qconfigDecorator.local;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalRollingWindowProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.local.LocalRollingWindowRateLimiter;

public class LocalRollingWindowDecorator implements QConfigDecorator {
    private LocalRollingWindowRateLimiter rateLimiter;
    private BasicProperties basicProperties;
    private LocalRollingWindowProperties localRollingWindowProperties;

    public LocalRollingWindowDecorator(JsonNode node) {
        this.setBasicProperties(node);
        this.setLocalRollingWindowProperties(node);
        this.init();
    }

    public void setLocalRollingWindowProperties(JsonNode node) {
        localRollingWindowProperties = new LocalRollingWindowProperties();
        localRollingWindowProperties.setMax(Long.parseLong(node.get("max").asText()));
        localRollingWindowProperties.setTime(Long.parseLong(node.get("time").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        rateLimiter = new LocalRollingWindowRateLimiter(basicProperties, localRollingWindowProperties);
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
