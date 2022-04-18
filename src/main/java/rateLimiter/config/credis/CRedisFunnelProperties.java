package rateLimiter.config.credis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CRedisFunnelProperties {
    private long qps;

    String strategyKey;
}
