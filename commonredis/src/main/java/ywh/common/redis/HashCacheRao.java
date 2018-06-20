package ywh.common.redis;

import java.util.List;
import java.util.Map;
/**
        * 可用于存放一个对象，替换ListCacheRao. 还可以用来计数
        * @author humortian
        * @date 2014-7-15
        */
public interface HashCacheRao {
    /**
     *
     * @param hashKey
     * @param type
     * @param members
     */
    public Boolean hdel(final Object hashKey, final String type, final String... members);
    /**
     * 如果不存在，长度为0
     * @param hashKey
     * @param type
     */
    public Long hlen(final Object hashKey, final String type);

    /**
     * 不管是否存在，都set，用户add, 不设过期时间
     * @param hashKey
     * @param fieldValues
     * @param type
     */
    public void hmset(final Object hashKey, final String type, final Map<String, String> fieldValues);

    /**
     * 不管是否存在，都set，用户add
     * @param hashKey
     * @param fieldValues
     * @param type
     * @param seconds
     */
    public void hmset(final Object hashKey, final String type, final Map<String, String> fieldValues, final int seconds);

    /**
     * 仅仅当key存在时，才set。用于update
     * 返回false表示key不存在
     * @param hashKey
     * @param fieldValues
     * @param type
     * @param seconds
     */
    public boolean hmsetx(final Object hashKey, final String type, final Map<String, String> fieldValues, final int seconds);

    /**
     * 仅仅当key存在时，才set。用于update
     * 返回false表示key不存在
     * @param hashKey
     * @param field
     * @param value
     * @param type
     * @param seconds
     * @return
     */
    public boolean hsetx(final Object hashKey, final String type, String field, String value, final int seconds);

    /**

     * @param hashKey
     * @param type
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public Long hset(final Object hashKey, final String type, String field, String value, final Integer seconds);

    /**
     * 仅仅当key不存在时，才set。用于重设缓存值
     * 返回false表示key存在
     * @param hashKey
     * @param type
     * @param fieldValues
     * @param seconds
     * @return
     */
    public boolean hmsetnx(final Object hashKey, final String type, final Map<String, String> fieldValues, int seconds);

    /**
     * 不做recover（依赖外部），单纯的在jedis.hsetnx的基础上加上了expire操作...（如果该key原本不存在的话）
     * @param hashKey
     * @param type
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hsetnx(Object hashKey, String type, String field, String value, Integer seconds);

    /**
     * 当key不存在，或者无field时，返回null
     * @param hashKey
     * @param type
     * @param fields
     * @return
     */
    public List<String> hmget(final Object hashKey, final String type, List<String> fields);

    /**
     * 当key不存在，或者无field时，返回null
     * @param hashKey
     * @param type
     * @param field
     * @return
     */
    public String hget(final Object hashKey, final String type, String field);

    public Map<String, String> hgetall(final Object hashKey, final String type);

    public Map<String, Long> hincreby(final Object hashKey, final String type, Map<String, Long> fieldValues, final int seconds);

    /**
     * 只有当key-field都存在时才执行操作...
     * ！！！依赖外部方式解决并发问题 和 数据有效问题： 目前应用场景为 外部逐field recover
     * @param hashKey
     * @param type
     * @param field
     * @param value
     * @return
     */
    public Long hincrbyx(Object hashKey, String type, String field, Long value);

    public void del(final Object hashKey, final String type);

    public Boolean existKey(final Object hashKey, final String type);
}
