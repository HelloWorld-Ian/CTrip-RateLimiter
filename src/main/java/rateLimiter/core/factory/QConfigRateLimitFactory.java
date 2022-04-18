package rateLimiter.core.factory;

import com.fasterxml.jackson.databind.JsonNode;
import qunar.tc.qconfig.client.MapConfig;
import rateLimiter.core.decorator.qconfigDecorator.credis.CRedisFunnelDecorator;
import rateLimiter.core.decorator.qconfigDecorator.credis.CRedisRollingWindowDecorator;
import rateLimiter.core.decorator.qconfigDecorator.credis.CRedisTimeWindowDecorator;
import rateLimiter.core.decorator.qconfigDecorator.credis.CRedisTokenBucketDecorator;
import rateLimiter.core.decorator.qconfigDecorator.local.LocalFunnelDecorator;
import rateLimiter.core.decorator.qconfigDecorator.local.LocalRollingWindowDecorator;
import rateLimiter.core.decorator.qconfigDecorator.local.LocalTimeWindowDecorator;
import rateLimiter.core.decorator.qconfigDecorator.local.LocalTokenBucketDecorator;
import rateLimiter.core.ratelimiter.RateLimiter;
import rateLimiter.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过qconfig初始化实例
 *
 * @author Iancy
 * @date 2022//4/5
 */
public class QConfigRateLimitFactory  {
    private static final Map<String, RateLimiter> cache = new HashMap<>();

    static  {
        MapConfig mapConfig = MapConfig.get("ratelimit.properties");
        mapConfig.addPropertiesListener(change -> {
            Map<String,String> configs = mapConfig.asMap();
            configs.forEach((s, s2) -> {
                if (change.isChange(s))
                    initRateLimiter(s,s2);
            });
        });
        Map<String,String> configs = mapConfig.asMap();
        configs.forEach(QConfigRateLimitFactory::initRateLimiter);
    }

    private static void initRateLimiter(String key,String config) {
        JsonNode node = JsonUtils.readTree(config);
        String mode = node.get("mode").textValue();
        String type = node.get("type").textValue();
        RateLimiter rateLimiter = null;
        if (mode != null && type != null && key != null) {
            if (Mode.C_REDIS.value.equals(mode)) {
                if (Type.TOKEN_BUCKET.value.equals(type))
                    rateLimiter = new CRedisTokenBucketDecorator(node);
                else if (Type.TIME_WINDOW.value.equals(type))
                    rateLimiter = new CRedisTimeWindowDecorator(node);
                else if (Type.FUNNEL.value.equals(type))
                    rateLimiter = new CRedisFunnelDecorator(node);
                else if (Type.ROLLING_WINDOW.value.equals(type))
                    rateLimiter = new CRedisRollingWindowDecorator(node);
            } else if (Mode.LOCAL.value.equals(mode)) {
                if (Type.TOKEN_BUCKET.value.equals(type))
                    rateLimiter = new LocalTokenBucketDecorator(node);
                else if (Type.TIME_WINDOW.value.equals(type))
                    rateLimiter = new LocalTimeWindowDecorator(node);
                else if (Type.FUNNEL.value.equals(type))
                    rateLimiter = new LocalFunnelDecorator(node);
                else if (Type.ROLLING_WINDOW.value.equals(type))
                    rateLimiter = new LocalRollingWindowDecorator(node);
            }
        }
        if (rateLimiter != null)
            cache.put(key, rateLimiter);
    }

    public static RateLimiter getInstance(String name) {
        return cache.get(name);
    }

    private enum Mode {
        C_REDIS("CRedis"),
        LOCAL("Local");
        String value;
        Mode(String value) {
            this.value = value;
        }
    }

    private enum Type {
        TOKEN_BUCKET("TokenBucket"),
        TIME_WINDOW("TimeWindow"),
        FUNNEL("Funnel"),
        ROLLING_WINDOW("RollingWindow");
        String value;
        Type(String value) {
            this.value = value;
        }
    }
}
