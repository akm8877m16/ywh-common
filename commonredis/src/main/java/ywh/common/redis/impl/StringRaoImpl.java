
package ywh.common.redis.impl;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RObject;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import ywh.common.redis.StringCacheRao;
import ywh.common.util.exception.DescribeException;
import ywh.common.util.exception.ExceptionEnum;
import java.util.concurrent.TimeUnit;

/**
 * @author tianhui
 * 如果只是单线程操作某个key，则是安全的，也没有分布式问题

    但一点可能存在多个进程操作同一个Ｋｅｙ的情况，就要加分布式锁

    线程之间加锁

 */

public class StringRaoImpl extends RedisBaseRaoImpl implements StringCacheRao {

    @Autowired
    protected RedissonClient redissonClient;


    public void set(String key, String value) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key+" , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    public void set(String key, String value, int seconds) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value,seconds, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key+" , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

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


    @Override
    public void set(Object stringKey, String type, String value) {
        String key = null;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key +" : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public void set(Object stringKey, String type, String value, int seconds) {
        String key = null;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value,seconds, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to set, key=" + key+" : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public String get(Object stringKey, String type) {
        String key = null;
        String res = null;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            res = bucket.get();
        } catch (RuntimeException e) {
            throw new DescribeException("fail to get, key=" + key + " : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
          return res;
        }

    }

    @Override
    public String getset(Object stringKey, String type, String value) {
        String key = null;
        String res = null;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            res = bucket.getAndSet(value);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to getset, key=" + key + " : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
            return res;
        }
    }

    @Override
    public Boolean expire(Object stringKey, String type, Integer seconds) {
        String key = null;
        Boolean res = false;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            res = bucket.expire(seconds,TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to expire, key=" + key + " : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
            return res;
        }
    }

    @Override
    public Long ttl(Object stringKey, String type) {
        String key = null;
        Long res = -1L;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            res = bucket.remainTimeToLive();
        } catch (RuntimeException e) {
            throw new DescribeException("fail to expire, key=" + key + " : " + e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
           return res;
        }
    }

    @Override
    public void del(Object stringKey, String type) {
        String key = null;
        try {
            key = getKey(stringKey, type);
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.delete();
        } catch (RuntimeException e) {
            String msg = "fail to del string, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public Long increx(Object stringKey, String type, int seconds) {
        String key = null;
        Long res = null;
        try {
            key = getKey(stringKey, type);
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            atomicLong.expire(seconds,TimeUnit.SECONDS);
            if(atomicLong.isExists()){
                res =  atomicLong.incrementAndGet();
            }
        } catch (RuntimeException e) {
            String msg = "fail to increx, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
           return res;
        }
    }


    @Override
    public Long decrex(Object stringKey, String type, int seconds) {
        String key = null;
        Long res = null;
        try {
            key = getKey(stringKey, type);
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            atomicLong.expire(seconds,TimeUnit.SECONDS);
            if(atomicLong.isExists()){
                res =  atomicLong.decrementAndGet();
            }
        } catch (RuntimeException e) {
            String msg = "fail to decrex, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
            return res;
        }
    }

    @Override
    public Long incre(Object stringKey, String type, int seconds) {
        String key = null;
        Long res = null;
        try {
            key = getKey(stringKey, type);
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            res = atomicLong.incrementAndGet();
            atomicLong.expire(seconds,TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            String msg = "fail to incre, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
           return res;
        }
    }

    @Override
    public boolean exist(final Object stringKey, final String type){
        String key = null;
        Boolean res = false;
        try {
            key = getKey(stringKey, type);
            RBucket<Object> bucket = redissonClient.getBucket(key);
            res = bucket.isExists();
        } catch (RuntimeException e) {
            String msg = "fail to exist, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
            return res;
        }
    }

    @Override
    public Long incre(Object stringKey, String type)
    {
        String key = null;
        Long res = null;
        try {
            key = getKey(stringKey, type);
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            res = atomicLong.incrementAndGet();
        } catch (RuntimeException e) {
            String msg = "fail to incre, key=" + key + " : "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {
            return res;
        }
    }

}

