package ywh.common.redis;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import ywh.common.redis.config.RedisConfig;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisConfig.class)
public class YwhCommonRedisAutoConfiguration {

    @Autowired
    private RedisConfig redisConfig;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if(redisConfig.getClusterServer()!=null){
            config.useClusterServers().setPassword(redisConfig.getPassword())
                    .addNodeAddress(redisConfig.getClusterServer().getNodeAddresses());
        }else {
            config.useSingleServer().setAddress(redisConfig.getAddress())
                    .setDatabase(redisConfig.getDatabase())
                    .setPassword(redisConfig.getPassword());
        }
        Codec codec=(Codec) ClassUtils.forName(redisConfig.getCodec(),ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }




}
