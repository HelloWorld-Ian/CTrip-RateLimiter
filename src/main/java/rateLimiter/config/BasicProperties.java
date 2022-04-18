package rateLimiter.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicProperties {
    private long timeout;
    private boolean limitRetry;
    private long retryPeriod;
}
