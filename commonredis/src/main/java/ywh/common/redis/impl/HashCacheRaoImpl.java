package ywh.common.redis.impl;

import jodd.datetime.TimeUtil;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.fn.timer.TimeUtils;
import sun.security.krb5.internal.crypto.Des;
import ywh.common.redis.HashCacheRao;
import ywh.common.util.exception.DescribeException;
import ywh.common.util.exception.ExceptionEnum;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 为了以后扩展，需要遵循twemproxy的使用方式，不出现批量命令，不出现事务等
 * @author humortian
 * @date 2014-7-16
 */
public class HashCacheRaoImpl extends RedisBaseRaoImpl implements HashCacheRao {
    @Autowired
    protected RedissonClient redissonClient;

    @Override
    public void hmset(Object hashKey, String type, Map<String, String> fieldValues) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            map.putAll(fieldValues);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hmset, key=" + key+", :"+ e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public void hmset(Object hashKey, String type,
                      Map<String, String> fieldValues, int seconds) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            map.putAll(fieldValues);
            map.expire(seconds, TimeUnit.SECONDS);

        } catch (RuntimeException e) {
            throw new DescribeException("fail to hmset, key=" + key+", :"+e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }


    @Override
    public boolean hmsetx(Object hashKey, String type,
                          Map<String, String> fieldValues, int seconds) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            map.expire(seconds, TimeUnit.SECONDS);
            if(map.isExists()){
                map.putAll(fieldValues);
            } else {
                return false;
            }

        } catch (RuntimeException e) {
            throw new DescribeException("fail to hmsetx, key=" + key+", "+e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return true;
    }

    @Override
    public Boolean hsetx(Object hashKey, String type, String field,
                         String value, int seconds) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            map.expire(seconds, TimeUnit.SECONDS);

            if(map.isExists()){
                map.put(field,value);
            } else {
                return false;
            }

        } catch (RuntimeException e) {
            throw new DescribeException("fail to hsetx, key=" + key+", "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return true;
    }

    @Override
    public Map<String,String> hmget(Object hashKey, String type, List<String> fields) {
        String key = null;

        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            Set<String> setFields = new HashSet<>(fields);
            Map<String,String> values = map.getAll(setFields);
            if(values.isEmpty()){
                return null;
            }
            return values;
        } catch (RuntimeException e) {

            throw new DescribeException("fail to hmget, key=" + key + ", fields=" + fields+", "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public String hget(Object hashKey, String type, String field) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            String value = map.get(field);
            if(null != value){
                return value;
            }
            return null;
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hget, key=" + key + ", : "+ e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public Map<String, String> hgetall(Object hashKey, String type) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            Map<String,String> mapFeed = map.getAll(map.keySet());
            if(mapFeed.isEmpty()){
                return null;
            }

            return mapFeed;
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hgetall, key=" + key + " : "+e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public String hincrbyx(Object hashKey, String type,
                         String field, Long value){
        String key = null;
        String newValue = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            if (map.get(field) != null){
                newValue = map.addAndGet(field,value);
            }
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hincreby, key=" + key + ", fv=" + value + " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return newValue;
    }

    @Override
    public Map<String, Long> hincreby(Object hashKey, String type,
                                      Map<String, Long> fieldValues, int seconds) {
        String key = null;
        Map<String, Long> newValues = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            //过期，再判断存在，是没有并发问题的。
            //但如果过期，再判断不存在，就有事务问题
            map.expire(seconds,TimeUnit.SECONDS);
            if(map.isExists()){
                newValues = new HashMap<String, Long>(fieldValues.size());
                for (Map.Entry<String, Long> entry : fieldValues.entrySet()) {
                    String value = map.addAndGet(entry.getKey(),entry.getValue());
                    newValues.put(entry.getKey(), Long.valueOf(value));
                }
            }

        } catch (RuntimeException e) {
            throw new DescribeException("fail to hincreby, key=" + key + ", fv=" + fieldValues + " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return newValues;
    }

    @Override
    public void del(Object hashKey, String type) {
        String key = null;
        Boolean res = false;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            res = map.delete();
        } catch (RuntimeException e) {
            String msg = "fail to del hash, key=" + key + " , "+e;
            throw new DescribeException(msg, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }


    @Override
    public String hset(Object hashKey, String type, String field,
                     String value, Integer seconds) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);

            String res = map.put(field,value);

            if(res != null &&seconds != null){
                map.expire(seconds,TimeUnit.SECONDS);
            }
            return res;
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hsetx, key=" + key + " , " + e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }

    }

    @Override
    public boolean hmsetnx(Object hashKey, String type, Map<String, String> fieldValues, int seconds) {
        //XXX: 由于使用了代理，不能使用transaction，会有并发问题
        //既然用了redisson, 可以使用锁确保并发问题
        String key = null;
        boolean res = false;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            if(map.isExists()){
                RLock lock = map.getLock(key);
                lock.lock(5,TimeUnit.SECONDS);
                map.putAll(fieldValues);
                res = map.expire(seconds,TimeUnit.SECONDS);
                lock.unlock();
            }
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hsetnx, key=" + key+" , :"+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return res;
    }

    @Override
    public boolean hsetnx(Object hashKey, String type, String field, String value, Integer seconds){
        Boolean resValue = null;
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            resValue = map.fastPutIfAbsent(field,value);
            if(resValue && seconds != null) map.expire(seconds,TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new DescribeException("fail to hsetnx, key=" + key + " , "+e,ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
        return resValue;
    }

    @Override
    public int hlen(Object hashKey, String type) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            if(!map.isExists()){
                return 0;
            }else{
                return map.size();
            }

        } catch (RuntimeException e) {
            throw new DescribeException("fail to hmset, key=" + key + " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public Boolean hdel(Object hashKey, String type, String... members) {
        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            if (map.isExists()) {
                map.fastRemove(members);
                return true;
            } else {
                return false;
            }

        } catch (RuntimeException e) {
            throw new DescribeException("fail to del members, key=" + key + " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }

    @Override
    public Boolean existKey(final Object hashKey, final String type) {

        String key = null;
        try {
            key = getKey(hashKey, type);
            RMap<String, String> map = redissonClient.getMap(key);
            return map.isExists();
        } catch (RuntimeException e) {
            throw new DescribeException("fail to check exist key, key=" + key + " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
        } finally {

        }
    }


}