


## 1. rate limiter 限流工具说明文档
### （1）demo
&emsp;&emsp;使用该工具前先用qconfig配置限流策略，配置文件名为ratelimit.properties <br>
&emsp;&emsp;1> 配置示例：
```properties
tokenBucketTest_Redis={"mode":"CRedis","type":"TokenBucket","timeout":10000,"limitRetry":false,"retryPeriod":10,"interval":1000,"capacity":60,"quantum":30,"strategyKey":"tokenBucketTest_Redis"}
timeWindowTest_Redis={"mode":"CRedis","type":"TimeWindow","timeout":10000,"limitRetry":false,"retryPeriod":10,"max":60,"time":1000,"strategyKey":"timeWindowTest_Redis"}
funnelTest_Redis={"mode":"CRedis","type":"Funnel","timeout":10000,"limitRetry":false,"retryPeriod":10,"qps":5,"strategyKey":"funnelTest_Redis"}
tokenBucketTest_Local={"mode":"Local","type":"TokenBucket","timeout":10000,"limitRetry":false,"retryPeriod":"10","interval":1000,"capacity":40,"quantum":20}
timeWindowTest_Local={"mode":"Local","type":"TimeWindow","timeout":10000,"limitRetry":false,"retryPeriod":10,"max":20,"time":1000}
funnelTest_Local={"mode":"Local","type":"Funnel","timeout":10000,"limitRetry":false,"retryPeriod":10,"qps":20}
```
&emsp;&emsp;2> 代码示例：
```java
import com.ctrip.ibu.market.common.rateLimiter.starter.QConfigRateLimitStarter;

class demo {
    public static void main(String[] args) {
        // 结合qconfig进行使用，使用qconfig配置限流策略
        if (QConfigRateLimitStarter.doRateLimit("tokenBucketTest_Redis")) {
            System.out.println("未被限流");
        } else {
            System.out.println("被限流");
        }
    }
}
```
### （2）限流模型
#### 1> 时间窗模型（time window）
* 配置参数 <br>
  redis限流：<br>
```
{
  "mode":"CRedis",  
  "type":"TimeWindow",
  "timeout":10000,   /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10, /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "max":60,     /* 单位时间窗内可通过的最大请求数 */
  "time":1000,  /* 单位时间窗长度，单位：毫秒 */
  "strategyKey":"timeWindowTest_Redis" /* redis中的存储key */
}
```
&emsp;&emsp;本地限流：
```
{
  "mode":"Local",  
  "type":"TimeWindow",
  "timeout":10000,   /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10, /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "max":60,     /* 单位时间窗内可通过的最大请求数 */
  "time":1000,  /* 单位时间窗长度，单位：毫秒 */
}
```

#### 2> 令牌桶模型（token bucket）
* 配置参数 <br>
  redis限流：
```
{
  "mode":"CRedis",  
  "type":"TokenBucket",
  "timeout":10000,    /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10,   /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "interval":1000,    /* 两次新增token之间的时间间隔，单位：毫秒 */
  "capacity":60,      /* 令牌桶的最大容量 */
  "quantum":30,       /* 每次token添加的添加数量 */
  "strategyKey":"tokenBucketTest_Redis"  /* redis中的存储key */
}
```
&emsp;&emsp;本地限流
```
{
  "mode":"Local",  
  "type":"TokenBucket",
  "timeout":10000,    /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10,   /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "interval":1000,    /* 两次新增token之间的时间间隔，单位：毫秒 */
  "capacity":60,      /* 令牌桶的最大容量 */
  "quantum":30,       /* 每次token添加的添加数量 */
}
```


#### 3> 漏斗模型（funnel）
* 配置参数 <br>
  redis限流：
```
{
  "mode":"CRedis",  
  "type":"Funnel",
  "timeout":10000,    /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10,   /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "qps":20,   /* 每秒通过的请求数 */
  "strategyKey":"tokenBucketTest_Redis"  /* redis中的存储key */
}
```
&emsp;&emsp;本地限流
```
{
  "mode":"Local",  
  "type":"Funnel",
  "timeout":10000,    /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10,   /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "qps":20,   /* 每秒通过的请求数 */
}
```
#### 4> 滑动窗口模型（rolling window）
* 配置参数 <br>
  redis限流：
```
{
  "mode":"CRedis",  
  "type":"TimeWindow",
  "timeout":10000,   /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10, /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "max":60,     /* 滑动窗口内可通过的请求数上限 */
  "time":1000,  /* 滑动窗口长度，单位：毫秒 */
  "strategyKey":"timeWindowTest_Redis" /* redis中的存储key */
}
```
&emsp;&emsp;本地限流：
```
{
  "mode":"Local",  
  "type":"TimeWindow",
  "timeout":10000,   /* 限流超时时间，超时后拒绝请求 */
  "limitRetry":false, /* 限流拒绝后是否重试 */
  "retryPeriod":10, /* 限流被拒绝后的重试间隔，单位：毫秒 */
  "max":60,     /* 滑动窗口内可通过的请求数上限 */
  "time":1000,  /* 滑动窗口长度，单位：毫秒 */
}
```
### （3）限流策略的可扩展性
#### 1> 自定义限流策略
&emsp;&emsp;该工具类实现了限流过程和限流策略的解耦，可以对限流策略进行自由扩展，只需要继承AbstractRateLimiter，实现limit()方法定义限流策略即可。扩展示例如下（local token bucket strategy）：<br>
```java
public interface RateLimiter {
    boolean limit() throws Exception;
    boolean doRateLimit() throws Exception;
}
```
```java
public abstract class AbstractRateLimiter implements RateLimiter {

    BasicProperties basicProperties;

    AbstractRateLimiter abstractRateLimiter;

    public AbstractRateLimiter(BasicProperties basicProperties) {
        this.basicProperties = basicProperties;
    }

    /**
     * the external method to do rate limit
     */
    public boolean doRateLimit() throws Exception {
        long waitTime = basicProperties.getTimeout();
        long timeout = waitTime + System.currentTimeMillis();
        boolean limitRetry = basicProperties.isLimitRetry();
        long retryPeriod = basicProperties.getRetryPeriod();
        boolean pass = false;
        while (!pass) {
            if (limit()) {
                pass = true;
            } else if (!limitRetry) {
                RateLimitLogger.infoLog("retry policy is forbidden, request is rejected");
                break;
            } else {
                long curTime = System.currentTimeMillis();
                if(curTime > timeout){
                    RateLimitLogger.infoLog("time out, request is rejected");
                    break;
                }
                Thread.sleep(retryPeriod);
            }
        }
        return pass;
    }
}
```
```java
public class LocalTokenBucketRateLimiter extends AbstractRateLimiter {
    private final Bucket bucket=new Bucket();

    public LocalTokenBucketRateLimiter(BasicProperties basicProperties, LocalTokenBucketProperties strategy){
        super(basicProperties);
        bucket.interval = strategy.getInterval();
        bucket.capacity = strategy.getCapacity();
        bucket.quantum = strategy.getQuantum();

        bucket.lastTick = System.currentTimeMillis();
        bucket.tokens = bucket.capacity;
    }

    @Override
    public synchronized boolean limit() {
        long increment =increment(bucket);
        long curToken = bucket.tokens;

        if (curToken > 0 || increment > 0){
            if(increment > 0){
                bucket.tokens = Math.min(bucket.capacity, curToken+increment) - 1;
                bucket.lastTick = System.currentTimeMillis();
            }else{
                bucket.tokens -= 1;
            }
            return true;
        }
        return false;
    }

    /**
     * the total tokens add to bucket during the last tick to current
     */
    private long increment(Bucket bucket){
        long cur = System.currentTimeMillis();
        long round = (cur - bucket.lastTick)/ bucket.interval;
        return round * bucket.quantum;
    }

    public static class Bucket{
        private long tokens;
        private long interval;
        private long lastTick;
        private long capacity;
        private long quantum;
        private Bucket(){}
    }
}

```
#### 2> 限流策略对接QConfig
&emsp;&emsp;为了实现核心功能的弱依赖性，该工具类将核心功能与外部配置文件实例化解耦，通过装饰类代理核心功能接入QConfig，接入方式如下：
```java
public interface QConfigDecorator extends RateLimiter {
    void init() throws Exception;
}
```
```java
public class LocalTokenBucketDecorator implements QConfigDecorator {
    private LocalTokenBucketRateLimiter localTokenBucketRateLimiter;
    private BasicProperties basicProperties;
    private LocalTokenBucketProperties localTokenBucketProperties;

    public LocalTokenBucketDecorator(JsonNode node) {
        this.setBasicProperties(node);
        this.setLocalTokenBucketProperties(node);
        this.init();
    }

    public void setLocalTokenBucketProperties(JsonNode node) {
        localTokenBucketProperties = new LocalTokenBucketProperties();
        localTokenBucketProperties.setCapacity(Long.parseLong(node.get("capacity").asText()));
        localTokenBucketProperties.setInterval(Long.parseLong(node.get("interval").asText()));
        localTokenBucketProperties.setQuantum(Long.parseLong(node.get("quantum").asText()));
    }

    public void setBasicProperties(JsonNode node) {
        basicProperties = new BasicProperties();
        basicProperties.setLimitRetry(Boolean.parseBoolean(node.get("limitRetry").asText()));
        basicProperties.setRetryPeriod(Long.parseLong(node.get("retryPeriod").asText()));
        basicProperties.setTimeout(Long.parseLong(node.get("timeout").asText()));
    }

    @Override
    public void init() {
        localTokenBucketRateLimiter = new LocalTokenBucketRateLimiter(basicProperties, localTokenBucketProperties);
    }


    @Override
    public boolean limit() throws Exception {
        return localTokenBucketRateLimiter.limit();
    }

    @Override
    public boolean doRateLimit() throws Exception {
        return localTokenBucketRateLimiter.doRateLimit();
    }
}

```
```java
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

```
