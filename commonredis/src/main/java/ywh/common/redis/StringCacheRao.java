package ywh.common.redis;

/**
 * 可用于存放一个字符串
 */
public interface StringCacheRao {


    /**
     * 不管是否存在，都set, 不设过期时间
     * @param stringKey
     * @param value
     */
    public void set(final Object stringKey, final String type, final String value);

    /**
     * 不管key是否存在，都set
     * @param stringKey
     * @param value
     * @param seconds
     */
    public void set(final Object stringKey, final String type, final String value, final int seconds);

    /**
     * 当key不存在，返回null
     * @param stringKey
     * @return
     */
    public String get(final Object stringKey, final String type);

    /**
     * 不敢key是否存在，都删除
     * @param stringKey
     * @param type
     */
    public void del(final Object stringKey, final String type);

    /**
     * 当key存在时，才计数++。如果计数失败，删除key
     */
    public Long increx(final Object stringKey, final String type, final int seconds);

    /**
     * 当key存在时，才计数--。如果计数失败，删除key
     */
    public Long decrex(final Object stringKey, final String type, final int seconds);

    /**
     * 不管key是否存在，计数++.用于消息通知，和清零。
     */
    public Long incre(final Object stringKey, final String type, final int seconds);

    /**
     * 不管key是否存在，计数++.用于消息通知，和清零。
     */
    public Long incre(final Object stringKey, final String type);

    /**
     * 判断key是否存在
     */
    public boolean exist(final Object stringKey, final String type);

    String getset(Object stringKey, String type, String value);

    Boolean expire(Object stringKey, String type, Integer seconds);

    Long ttl(Object stringKey, String type);


}
