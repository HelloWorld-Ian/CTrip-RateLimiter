package rateLimiter.core.decorator.qconfigDecorator.local;

import com.fasterxml.jackson.databind.JsonNode;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalFunnelProperties;
import rateLimiter.core.decorator.qconfigDecorator.QConfigDecorator;
import rateLimiter.core.ratelimiter.local.LocalFunnelRateLimiter;

public class LocalFunnelDecorator implements QConfigDecorator {
    private LocalFunnelRateLimiter localFunnelRateLimiter;
    private BasicProperties basicProperties;
    private LocalFunnelProperties localFunnelProperties;

    public LocalFunnelDecorator(JsonNode node) {
        this.setLocalFunnelProperties(node);
        this.setBasicProperties(node);
        this.init();
    }

    public void setLocalFunnelProperties(JsonNode node) {
        localFunnelProperties = new LocalFunnelProperties();
        localFunnelProperties.setQps(Long.parseLong(node.get("qps").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        localFunnelRateLimiter = new LocalFunnelRateLimiter(basicProperties, localFunnelProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return localFunnelRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return localFunnelRateLimiter.doRateLimit();
    }
}
