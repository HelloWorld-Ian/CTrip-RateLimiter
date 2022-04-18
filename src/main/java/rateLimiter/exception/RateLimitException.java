package rateLimiter.exception;

public class RateLimitException extends Exception {
    public RateLimitException(String str) {
        super(str);
    }
}
