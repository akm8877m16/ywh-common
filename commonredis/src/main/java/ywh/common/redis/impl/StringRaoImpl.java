
package ywh.common.redis.impl;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import ywh.common.redis.StringCacheRao;
import ywh.common.util.exception.DescribeException;
import ywh.common.util.exception.ExceptionEnum;
import java.util.concurrent.TimeUnit;

/**
 * @author tianhui
 *
 */

public class StringRaoImpl extends RedisBaseRaoImpl implements StringCacheRao {

    @Autowired
    protected RedissonClient redissonClient;

    @Override
    public void set(String key, String value) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key+" , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value,seconds, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key+" , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public String get(String key) {
        String res = null;
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            res = bucket.get();
        } catch (RuntimeException e) {
            throw new DescribeException("fail to get, key=" + key+" , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
           return res;
        }
    }

    @Override
    public void del(String key) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.delete();
        } catch (RuntimeException e) {
            String msg = "fail to del string, key=" + key + " , "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

}

