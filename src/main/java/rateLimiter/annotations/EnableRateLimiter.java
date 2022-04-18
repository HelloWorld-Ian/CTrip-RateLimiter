package rateLimiter.annotations;


import org.springframework.context.annotation.Import;
import rateLimiter.springConfigration.RateLimiterBeanRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RateLimiterBeanRegister.class})
public @interface EnableRateLimiter {
    String redisClusterName() default "";
}
