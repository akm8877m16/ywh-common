package ywh.common.redis;

/**
 * @param <T> info的数据结构
 * @param <PK> 主键
 */
public interface GenericInfoRao<T, PK> {

    void add(T entity);

    T get(PK id) ;

    /**
     * 通过唯一键查找，domain必须有至少一个RedisField指定inUniqueKey
     * @param t
     * @return
     */
    T getUnique(T t);

    /**
     * 当key不存在，或者无field时，返回null
     * @param id
     * @param prop
     * @return
     */
    String get(PK id, String prop);


    /**
     * 容许value为null的更新。容许删除field逻辑
     * @param id
     * @param field
     * @param value
     * @return
     */
    Boolean update(PK id, String field, String value);

    /**
     * 容许value为null的更新。容许删除field逻辑
     * 当skipMissingProperty为true时忽略不存在的property
     * @param entity
     * @param skipMissingProperty
     * @param props
     * @return
     */
    Boolean update(T entity, boolean skipMissingProperty, String... props);

    /**
     * 忽略null的更新
     * @param entity
     */
    void update(T entity);

    /**
     * update多个fields，属性为fields，value在entity中取值。
     * 不容许删除field逻辑，
     * @param entity
     * @param fields
     * @return
     */
    Boolean update(T entity, String... fields);

    Long incre(PK id, String field, int increCount);

    void delKey(PK id);

    void delUniqueKey(T t);

    boolean existKey(T t);
}