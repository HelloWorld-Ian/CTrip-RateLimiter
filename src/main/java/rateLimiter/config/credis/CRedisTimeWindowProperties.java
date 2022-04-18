package rateLimiter.config.credis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CRedisTimeWindowProperties {
    long max;
    long time;

    String strategyKey;
}
