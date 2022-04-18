package rateLimiter.config.local;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalTokenBucketProperties {
    long interval;
    long capacity;
    long quantum;
}
