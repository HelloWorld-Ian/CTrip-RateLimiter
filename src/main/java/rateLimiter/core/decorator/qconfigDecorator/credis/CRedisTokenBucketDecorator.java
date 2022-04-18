package rateLimiter.core.decorator.qconfigDecorator.credis;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisTokenBucketProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.credis.CRedisTokenBucketRateLimiter;

public class CRedisTokenBucketDecorator implements QConfigDecorator {
    private CRedisTokenBucketRateLimiter cRedisTokenBucketRateLimiter;
    private CRedisTokenBucketProperties cRedisTokenBucketProperties;
    private BasicProperties basicProperties;

    public CRedisTokenBucketDecorator(JsonNode node) {
        this.setCRedisTokenBucketProperties(node);
        this.setBasicProperties(node);
        this.init();
    }

    public void setCRedisTokenBucketProperties(JsonNode node) {
        cRedisTokenBucketProperties = new CRedisTokenBucketProperties();
        cRedisTokenBucketProperties.setCapacity(Long.parseLong(node.get("capacity").asText()));
        cRedisTokenBucketProperties.setInterval(Long.parseLong(node.get("interval").asText()));
        cRedisTokenBucketProperties.setQuantum(Long.parseLong(node.get("quantum").asText()));
        cRedisTokenBucketProperties.setStrategyKey(node.get("strategyKey").asText());
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        cRedisTokenBucketRateLimiter = new CRedisTokenBucketRateLimiter(basicProperties,cRedisTokenBucketProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return cRedisTokenBucketRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return cRedisTokenBucketRateLimiter.doRateLimit();
    }
}
