package ywh.common.redis;

/**
 利用RKeys 对象实现对key的搜索
 **/

public interface KeysRao {

    public Iterable<String> getKeys(final String pattern);

    public Long keysCount(final String pattern);

}
