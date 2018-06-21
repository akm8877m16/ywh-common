package ywh.common.redis;

/**
 * 可用于存放一个字符串
 */
public interface StringCacheRao {

    /**
     * 不管是否存在，都set, 不设过期时间
     * @param key
     * @param value
     */
    public void set(final String key, final String value);

    /**
     * 不管key是否存在，都set
     */
    public void set(final String key, final String value, final int seconds);

    /**
     * 当key不存在，返回null
     */
    public String get(final String key);

    /**
     * 不敢key是否存在，都删除
     */
    public void del(final String key);
}
