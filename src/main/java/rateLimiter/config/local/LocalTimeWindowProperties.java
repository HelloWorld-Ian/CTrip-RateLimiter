package rateLimiter.config.local;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalTimeWindowProperties {
    private long time;
    private long max;
}
