package ywh.common.redis.impl;

import jodd.util.StringUtil;

/**
 * 为了以后扩展，需要遵循twemproxy的使用方式，不出现批量命令，不出现事务等
 * @author humortian
 * @date 2014-7-16
 * change Jedis to Redisson
 * @auther wenhao Yin
 * @date 2018-06-20
 */
public abstract class RedisBaseRaoImpl {

    protected String getKey(final Object key, final String type){
        if (StringUtil.isEmpty(type)){
            return key.toString();
        }
        return type + ":{" + key.toString() + "}";
    }

}

