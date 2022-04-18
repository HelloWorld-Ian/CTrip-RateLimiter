package rateLimiter.springConfigration;


import credis.java.client.config.ClusterLevelConfig;
import credis.java.client.config.impl.DefaultClusterLevelConfig;
import credis.java.client.util.CacheFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import rateLimiter.annotations.EnableRateLimiter;
import rateLimiter.utils.RedisClientUtils;

import java.util.Map;

/**
 * 限流注解处理器,针对Spring应用开发，指定redis集群
 */
public class RateLimiterBeanRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String,Object>attributes= annotationMetadata.getAnnotationAttributes(EnableRateLimiter.class.getName());
        String redisClusterName = attributes.get("redisClusterName").toString();
        ClusterLevelConfig
                config = DefaultClusterLevelConfig.newBuilder().setRemoveAbandonedOnMaintenance(false).build();
        RedisClientUtils.setCacheProvider(CacheFactory.getProvider(redisClusterName,config));
    }
}
