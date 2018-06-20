/*
package ywh.common.redis.impl;

import ywh.common.redis.StringRao;

/**
 * @author tianhui
 *
 */
/*
public class StringRaoImpl extends RedisBaseRaoImpl implements StringRao {

    @Override
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (RuntimeException e) {
            closeBrokenJedis(jedis);
            jedis = null;
            throw new RedisRuntimeException("fail to set, key=" + key, e);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            jedis.expire(key, seconds);
        } catch (RuntimeException e) {
            closeBrokenJedis(jedis);
            jedis = null;
            throw new RedisRuntimeException("fail to set, key=" + key, e);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (RuntimeException e) {
            closeBrokenJedis(jedis);
            jedis = null;
            throw new RedisRuntimeException("fail to get, key=" + key, e);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();

            jedis.del(key);
        } catch (RuntimeException e) {
            closeBrokenJedis(jedis);
            jedis = null;

            String msg = "fail to del string, key=" + key;
            throw new RedisRuntimeException(msg, e);
        } finally {
            closeJedis(jedis);
        }
    }

}

*/