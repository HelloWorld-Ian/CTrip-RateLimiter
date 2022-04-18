package rateLimiter.core.decorator.qconfigDecorator.credis;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.credis.CRedisFunnelProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.credis.CRedisFunnelRateLimiter;

public class CRedisFunnelDecorator implements QConfigDecorator {
    private CRedisFunnelRateLimiter cRedisFunnelRateLimiter;
    private BasicProperties basicProperties;
    private CRedisFunnelProperties cRedisFunnelProperties;

    public CRedisFunnelDecorator(JsonNode node) {
        this.setCRedisFunnelProperties(node);
        this.setBasicProperties(node);
        this.init();
    }

    public void setCRedisFunnelProperties(JsonNode node) {
        cRedisFunnelProperties = new CRedisFunnelProperties();
        cRedisFunnelProperties.setStrategyKey(node.get("strategyKey").asText());
        cRedisFunnelProperties.setQps(Long.parseLong(node.get("qps").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        cRedisFunnelRateLimiter = new CRedisFunnelRateLimiter(basicProperties, cRedisFunnelProperties);
    }

    @Override
    public boolean limit() throws Exception {
        return cRedisFunnelRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return cRedisFunnelRateLimiter.doRateLimit();
    }
}
