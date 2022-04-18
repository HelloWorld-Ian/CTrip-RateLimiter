package rateLimiter.config.local;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalRollingWindowProperties {
    long max;
    long time;
}
