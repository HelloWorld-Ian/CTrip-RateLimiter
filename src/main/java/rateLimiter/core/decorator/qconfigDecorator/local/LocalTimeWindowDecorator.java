package rateLimiter.core.decorator.qconfigDecorator.local;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalTimeWindowProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.local.LocalTimeWindowRateLimiter;

public class LocalTimeWindowDecorator implements QConfigDecorator {
    private LocalTimeWindowRateLimiter localTimeWindowRateLimiter;
    private LocalTimeWindowProperties localTimeWindowProperties;
    private BasicProperties basicProperties;

    public LocalTimeWindowDecorator(JsonNode node) {
        this.setBasicProperties(node);
        this.setLocalTimeWindowProperties(node);
        this.init();
    }

    public void setLocalTimeWindowProperties(JsonNode node) {
        localTimeWindowProperties = new LocalTimeWindowProperties();
        localTimeWindowProperties.setTime(Long.parseLong(node.get("time").asText()));
        localTimeWindowProperties.setMax(Long.parseLong(node.get("max").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        localTimeWindowRateLimiter = new LocalTimeWindowRateLimiter(basicProperties, localTimeWindowProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return localTimeWindowRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return localTimeWindowRateLimiter.doRateLimit();
    }
}
