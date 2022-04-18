package rateLimiter.core.decorator.qconfigDecorator.local;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalTokenBucketProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.local.LocalTokenBucketRateLimiter;

public class LocalTokenBucketDecorator implements QConfigDecorator {
    private LocalTokenBucketRateLimiter localTokenBucketRateLimiter;
    private BasicProperties basicProperties;
    private LocalTokenBucketProperties localTokenBucketProperties;

    public LocalTokenBucketDecorator(JsonNode node) {
        this.setBasicProperties(node);
        this.setLocalTokenBucketProperties(node);
        this.init();
    }

    public void setLocalTokenBucketProperties(JsonNode node) {
        localTokenBucketProperties = new LocalTokenBucketProperties();
        localTokenBucketProperties.setCapacity(Long.parseLong(node.get("capacity").asText()));
        localTokenBucketProperties.setInterval(Long.parseLong(node.get("interval").asText()));
        localTokenBucketProperties.setQuantum(Long.parseLong(node.get("quantum").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        localTokenBucketRateLimiter = new LocalTokenBucketRateLimiter(basicProperties, localTokenBucketProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return localTokenBucketRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return localTokenBucketRateLimiter.doRateLimit();
    }
}
