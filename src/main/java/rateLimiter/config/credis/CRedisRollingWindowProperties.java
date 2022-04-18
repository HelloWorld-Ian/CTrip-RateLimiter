package rateLimiter.config.credis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CRedisRollingWindowProperties {
    long max;
    long time;

    String strategyKey;
}
