package rateLimiter.config.credis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CRedisTokenBucketProperties {
    long interval;
    long capacity;
    long quantum;

    String strategyKey;
}
