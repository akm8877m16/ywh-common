package ywh.common.redis.test.redisTest.config;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ywh.common.redis.HashCacheRao;
import ywh.common.redis.KeysRao;
import ywh.common.redis.StringCacheRao;
import ywh.common.redis.globalIdGenerator.IdGenerator;
import ywh.common.redis.impl.HashCacheRaoImpl;
import ywh.common.redis.impl.KeysRaoImpl;
import ywh.common.redis.impl.StringRaoImpl;
import ywh.common.redis.test.redisTest.domain.Device;
import ywh.common.redis.test.redisTest.raoImpl.DeviceRaoImpl;

import javax.annotation.Resource;

@Configuration
public class RaoImplConfig {

    @Resource
    private RedissonClient redissonClient;

    @Bean
    public HashCacheRao getHashCacheRao(){
        HashCacheRaoImpl hashCacheRao = new HashCacheRaoImpl();
        hashCacheRao.setRedissonClient(redissonClient);
        return hashCacheRao;
    }

    @Bean
    public StringCacheRao getStringCacheRao(){
        StringRaoImpl stringCacheRao = new StringRaoImpl();
        stringCacheRao.setRedissonClient(redissonClient);
        return stringCacheRao;
    }

    @Bean
    public KeysRao getKeysRao(){
        KeysRaoImpl keysRao = new KeysRaoImpl();
        keysRao.setRedissonClient(redissonClient);
        return keysRao;
    }



    @Bean("deviceRao")
    public DeviceRaoImpl getDeviceRaoImpl(){
        DeviceRaoImpl deviceRao = new DeviceRaoImpl();
        deviceRao.setHashCacheRao(getHashCacheRao());
        deviceRao.setStringCacheRao(getStringCacheRao());
        deviceRao.setKeysRao(getKeysRao());
        deviceRao.setSeconds(600);
        deviceRao.setKeyPrefix("device");
        return deviceRao;
    }

    @Bean("idGenerator")
    public IdGenerator getIdGenerator(){
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setIdName("deviceStatusIdGenerator");
        return idGenerator;
    }

}
