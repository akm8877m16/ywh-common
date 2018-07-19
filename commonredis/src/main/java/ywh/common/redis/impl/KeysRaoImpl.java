package ywh.common.redis.impl;

import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import ywh.common.redis.KeysRao;

public class KeysRaoImpl implements KeysRao {

    @Autowired
    protected RedissonClient redissonClient;

    @Override
    public Iterable<String> getKeys(String pattern){
        RKeys keys = redissonClient.getKeys();
        Iterable<String> foundedKeys = keys.getKeysByPattern(pattern);
        return foundedKeys;
    }

    @Override
    public Integer keysCount(String pattern){
        RKeys keys = redissonClient.getKeys();
        Iterable<String> foundedKeys = keys.getKeysByPattern(pattern);
        Integer count = 0;
        for(String key:foundedKeys){
            count++;
        }
        return count;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
