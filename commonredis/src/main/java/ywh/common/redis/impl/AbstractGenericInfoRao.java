
package ywh.common.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ywh.common.redis.GenericInfoRao;
import ywh.common.redis.HashCacheRao;
import ywh.common.redis.KeysRao;
import ywh.common.redis.StringCacheRao;
import ywh.common.redis.annotation.RedisEntity;
import ywh.common.redis.annotation.RedisField;
import ywh.common.redis.annotation.RedisId;
import ywh.common.util.exception.DescribeException;
import ywh.common.util.exception.ExceptionEnum;
import ywh.common.util.json.JsonUtil;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;


/**
 * @param <T>
 * @param <PK>
 */

public abstract class AbstractGenericInfoRao<T extends Serializable, PK> implements GenericInfoRao<T, PK> {

        protected static final Logger logger = LoggerFactory.getLogger(AbstractGenericInfoRao.class);

        private HashCacheRao hashCacheRao;

        private StringCacheRao stringCacheRao;

        private KeysRao keysRao;

        private Integer seconds;

        private String keyPrefix;

        //private String entityName;
        private Map<String, String> propNameToRedisFieldName;
        private Map<String, Field> redisFieldNameToField;
        private Field idRedisField;
        private Map<String, Field> uniqueIndexRedisFields;

        protected Class<T> entityClass;
        protected Class<PK> idClass;

        @SuppressWarnings("unchecked")
        public AbstractGenericInfoRao(){
            Class<?> c = getClass();
            Type t = c.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] p = ((ParameterizedType) t).getActualTypeArguments();
                this.entityClass = (Class<T>) p[0];
                this.idClass = (Class<PK>) p[1];
            }

            parseEntity(entityClass);
        }

        @Override
        public void add(T entity) {
            try {
                String hashKey = String.valueOf(idRedisField.get(entity));

                Set<String> props = propNameToRedisFieldName.keySet();
                Map<String, String> fieldValues = convert2Map(entity,
                        props.toArray(new String[props.size()]), false, false);

                if(!fieldValues.isEmpty()){
                    hashCacheRao.hmset(hashKey, keyPrefix, fieldValues, seconds);
                    if(uniqueIndexRedisFields != null) {
                        String stringIndex = getUniqueIndex(entity);
                        stringCacheRao.set(stringIndex, getIndexType(), hashKey, seconds);
                    }
                } else {
                    throw new DescribeException("entity can't all be null!, t=" + JsonUtil.getJsonFromObject(entity),ExceptionEnum.REDIS_ERROR.getCode());
                }
            } catch (Exception e) {
                throw new DescribeException("add redis error: "+e,ExceptionEnum.REDIS_ERROR.getCode());
            }

        }

        /**
         * @param entity
         * @return
         * @throws IllegalAccessException
         */

        private Map<String, String> convert2Map(T entity, String[] props, boolean skipMissingProperty, boolean allowNull){
            try {
                Map<String, String> fieldValues = new HashMap<String, String>();

                for(String prop:props){
                    prop = prop.toLowerCase();
                    String redisFieldName = propNameToRedisFieldName.get(prop);
                    if(redisFieldName==null){
                        if (skipMissingProperty) continue;
                        throw new DescribeException(prop+" doesn't match any field!", ExceptionEnum.REDIS_ERROR.getCode());
                    }
                    Field field = redisFieldNameToField.get(redisFieldName);
                    Object obj = field.get(entity);
                    String value;

                    if(obj == null){
                        if (allowNull) {
                            value = null;
                        } else {
                            continue;
                        }
                    } else if(obj instanceof Date){
                        value = String.valueOf(((Date) obj).getTime());
                    } else if(obj.getClass().isArray()){
                        if (obj.getClass().getComponentType() == byte.class) {
                            value = new String((byte[])obj, "ISO-8859-1");
                        } else {
                            value = JsonUtil.getJsonFromObject(field.get(entity));
                        }
                    } else {
                        value = String.valueOf(obj);
                    }

                    fieldValues.put(redisFieldName, value);
                }
                return fieldValues;
            } catch (Exception e) {
                throw new DescribeException("add redis error: "+e, ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        @Override
        public T get(PK id) {
            Map<String, String> redisMap = hashCacheRao.hgetall(id, keyPrefix);

            T obj = convert2Domain(id, redisMap);

            return obj;
        }

        @Override
        public T getUnique(T entity) {
            try{
                String index = getUniqueIndex(entity);
                String hashKey = stringCacheRao.get(index, getIndexType());
                if(hashKey == null){
                    return null;
                }
                Map<String, String> redisMap = hashCacheRao.hgetall(hashKey, keyPrefix);

                PK id = null;
                if(idClass == String.class){
                    id = (PK) hashKey;
                }else if(idClass == Long.class){
                    id = (PK) Long.valueOf(hashKey);
                }else if(idClass == Integer.class){
                    id = (PK) Integer.valueOf(hashKey);
                }else{
                    throw new DescribeException("unsupported type ",ExceptionEnum.REDIS_ERROR.getCode());
                }

                T obj = convert2Domain(id, redisMap);

                return obj;
            } catch (Exception e) {
                throw new DescribeException("get error"+" : "+e,ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        /**
         * @param id
         * @param redisMap
         * @return
         * @throws InstantiationException
         * @throws IllegalAccessException
         */

        private T convert2Domain(PK id, Map<String, String> redisMap) {
            if (redisMap == null) {
                return null;
            }
            try {
                T obj = entityClass.newInstance();

                for(String prop:propNameToRedisFieldName.keySet()){
                    String redisFieldName = propNameToRedisFieldName.get(prop);
                    Field field = redisFieldNameToField.get(redisFieldName);
                    String value = redisMap.get(redisFieldName);

                    if(value==null){
                        continue;
                    }

                    Class<?> type = field.getType();
                    if(type == String.class){
                        field.set(obj, value);
                    } else if (type == Date.class){
                        //field.set(obj, new Date.(value));
                    } else if (type == Long.class || type.getName().equalsIgnoreCase("long")){
                        field.set(obj, Long.valueOf(value));
                    } else if (type == Integer.class || type.getName().equalsIgnoreCase("int")){
                        field.set(obj, Integer.valueOf(value));
                    } else if (type == Short.class || type.getName().equalsIgnoreCase("short")){
                        field.set(obj, Short.valueOf(value));
                    } else if (type == Byte.class || type.getName().equalsIgnoreCase("byte")){
                        field.set(obj, Byte.valueOf(value));
                    } else if (type == Boolean.class || type.getName().equalsIgnoreCase("boolean")){
                        field.set(obj, Boolean.valueOf(value));
                    } else if (type == Float.class || type.getName().equalsIgnoreCase("float")){
                        field.set(obj, Float.valueOf(value));
                    } else if (type == Double.class || type.getName().equalsIgnoreCase("double")){
                        field.set(obj, Double.valueOf(value));
                    } else if (type == BigDecimal.class || type.getName().equalsIgnoreCase("bigdecimal")){
                        field.set(obj, new BigDecimal(value));
                    } else if (type.isArray()){
                        if (field.getType().getComponentType() == byte.class) {
                            field.set(obj, value.getBytes("ISO-8859-1"));
                        } else {
                            field.set(obj, JsonUtil.parserJsonArray(value, field.getType().getComponentType()));
                        }
                    } else {
                        throw new DescribeException("unsupported type "
                                + type + " row data = "+ value, ExceptionEnum.REDIS_ERROR.getCode());
                    }
                }

                if (id != null) {
                    idRedisField.set(obj, id);
                }
                return obj;
            } catch (Exception e) {
                throw new DescribeException("get error"+" : "+e,ExceptionEnum.REDIS_ERROR.getCode());
            }

        }

        @Override
        public String get(PK id, String prop){
            prop = prop.toLowerCase();
            String redisFieldName = propNameToRedisFieldName.get(prop);

            String value = null;
            if(redisFieldName!=null){
                value = hashCacheRao.hget(id, keyPrefix, redisFieldName);
            } else {
                logger.warn("unknown field when get, keyPrefix={}, filed={}", keyPrefix, prop);
            }

            return value;
        }

        @Override
        public List<T> findByKeyPattern(String pattern){
            List<T> result = new ArrayList<T>();
            Iterable<String> keys = keysRao.getKeys(pattern);
            for(String key : keys){
                String hashKey = stringCacheRao.get(key);
                try{
                    if(hashKey == null){
                        continue;
                    }
                    Map<String, String> redisMap = hashCacheRao.hgetall(hashKey, keyPrefix);

                    PK id = null;
                    if(idClass == String.class){
                        id = (PK) hashKey;
                    }else if(idClass == Long.class){
                        id = (PK) Long.valueOf(hashKey);
                    }else if(idClass == Integer.class){
                        id = (PK) Integer.valueOf(hashKey);
                    }else{
                        throw new DescribeException("unsupported type ",ExceptionEnum.REDIS_ERROR.getCode());
                    }

                    T obj = convert2Domain(id, redisMap);
                    result.add(obj);
                } catch (Exception e) {
                    throw new DescribeException("get error"+" : "+e,ExceptionEnum.REDIS_ERROR.getCode());
                }
            }
            return result;
        }


        @Override
        public Boolean update(PK id, String prop, String value){//TODO:想清掉一些null
            prop = prop.toLowerCase();
            String redisFieldName = propNameToRedisFieldName.get(prop);

            if(redisFieldName==null){
                throw new DescribeException(prop+" doesn't match any field!"+" : "+ entityClass,
                        ExceptionEnum.REDIS_ERROR.getCode());
            }

            if(value==null){
                return hashCacheRao.hdel(id, keyPrefix, prop);
            } else {
                return hashCacheRao.hsetx(id, keyPrefix, redisFieldName, value, seconds);
            }
        }


        @Override
        public void update(T entity) {
            update(entity, false, false, propNameToRedisFieldName.keySet().toArray(new String[propNameToRedisFieldName.keySet().size()]));
        }

        @Override
        public Boolean update(T entity, String... props) {
            return update(entity, false, props);
        }

        @Override
        public Boolean update(T entity, boolean skipMissingProperty, String... props) {
            return update(entity, skipMissingProperty, true, props);
        }

        public Boolean update(T entity, boolean skipMissingProperty, boolean updateNull, String... props) {
            try {
                String hashKey = String.valueOf(idRedisField.get(entity));

                Map<String, String> fieldValues = convert2Map(entity, props, skipMissingProperty, updateNull);
                if (updateNull) {
                    Set<String> toDel = new HashSet<>();
                    for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
                        if (entry.getValue() == null) {
                            hashCacheRao.hdel(hashKey, keyPrefix, entry.getKey());
                            toDel.add(entry.getKey());
                        }
                    }
                    for (String key: toDel) {
                        fieldValues.remove(key);
                    }
                }

                if (skipMissingProperty && fieldValues.isEmpty()) {
                    return hashCacheRao.existKey(hashKey, keyPrefix);
                }
                if(!fieldValues.isEmpty()){

                    //T obj = convert2Domain(hashKey, redisMap);
                    String oldIndex = "";
                    String index = "";
                    if(uniqueIndexRedisFields != null) {
                        oldIndex = getUniqueIndexForUpdate(hashKey);
                        index = getUniqueIndex(entity);
                    }
                    boolean result = hashCacheRao.hmsetx(hashKey, keyPrefix, fieldValues, seconds);
                    if(result && !oldIndex.equals(index)){
                        if(!oldIndex.isEmpty()) {
                            stringCacheRao.del(oldIndex, getIndexType());
                        }
                        stringCacheRao.set(index, getIndexType(), hashKey, seconds);
                    }
                    return result;
                } else {
                    throw new DescribeException("value of "+props+" cann't all be null!, t=" + JsonUtil.getJsonFromObject(entity), ExceptionEnum.REDIS_ERROR.getCode());
                }
            } catch (Exception e) {
                throw new DescribeException("update error "+ " , "+e, ExceptionEnum.REDIS_ERROR.getCode());
            }

        }

        @Override
        public Long incre(PK id, String prop, int increCount){
            prop = prop.toLowerCase();
            String redisFieldName = propNameToRedisFieldName.get(prop);

            if(redisFieldName==null){
                throw new DescribeException(prop+" doesn't match any field!" + " "+ entityClass,ExceptionEnum.REDIS_ERROR.getCode());
            }

            Field field = redisFieldNameToField.get(redisFieldName);
            Class<?> type = field.getType();

            if (type == Long.class || type.getName().equalsIgnoreCase("long") || type == Integer.class
                    || type.getName().equalsIgnoreCase("int") || type == Short.class
                    || type.getName().equalsIgnoreCase("short")){
                Map<String, Long> map = new HashMap<String, Long>(1);
                map.put(redisFieldName, (long)increCount);

                Map<String, Long> res =  hashCacheRao.hincreby(id, keyPrefix, map, seconds);
                if(res!= null){
                    return res.get(redisFieldName);
                } else {
                    return null;
                }
            } else {
                throw new DescribeException("value of "+prop+" cann't be null!"+" , "+entityClass, ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        @Override
        public void delKey(PK id){
            hashCacheRao.del(id, keyPrefix);
        }

        @Override
        public boolean existKey(T entity) {
            try {
                String hashKey = String.valueOf(idRedisField.get(entity));
                return hashCacheRao.existKey(hashKey, keyPrefix);
            } catch (Exception e) {
                throw new DescribeException("check key error"+" , "+ e, ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        @Override
        public void delUniqueKey(T entity){
            String index = getUniqueIndex(entity);
            String hashKey = stringCacheRao.get(index, getIndexType());
            hashCacheRao.del(hashKey, keyPrefix);
            stringCacheRao.del(index, getIndexType());
        }

        protected String getUniqueIndex(T entity) {
            if (uniqueIndexRedisFields == null) {
                throw new DescribeException("domain is not referred by unique index!"+", "+ entityClass, ExceptionEnum.REDIS_ERROR.getCode());
            }
            StringBuilder sb = new StringBuilder();
            try {
                for (Map.Entry<String, Field> uniqEntry : uniqueIndexRedisFields.entrySet()) {
                    Object value = uniqEntry.getValue().get(entity);
                    if (value != null) {
                        if (sb.length() > 0) {
                            sb.append(":");
                        }
                        sb.append(value);
                    } else {
                        throw new DescribeException("not all fields for unique index are provided:"+uniqEntry.getKey()+", "+ entityClass, ExceptionEnum.REDIS_ERROR.getCode());
                    }
                }
                return sb.toString();
            } catch (IllegalAccessException e) {
                throw new DescribeException("failed to access unique index on domain! "+ entityClass,ExceptionEnum.REDIS_ERROR.getCode());
            } catch (IllegalArgumentException e) {
                throw new DescribeException("failed to get value for unique index on domain! "+entityClass,ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        protected String getUniqueIndexForUpdate(String hashKey) {
            if (uniqueIndexRedisFields == null) {
                throw new DescribeException("domain is not referred by unique index! "+entityClass,ExceptionEnum.REDIS_ERROR.getCode());
            }
            StringBuilder sb = new StringBuilder();
            try {
                Map<String, String> redisMap = hashCacheRao.hgetall(hashKey, keyPrefix);
                if (redisMap == null){
                    return "";
                }
                for (Map.Entry<String, Field> uniqEntry : uniqueIndexRedisFields.entrySet()) {
                    String value = redisMap.get(uniqEntry.getKey());
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    sb.append(value);
                }
                return sb.toString();
            } catch (IllegalArgumentException e) {
                throw new DescribeException("failed to get value for unique index on domain! "+entityClass,ExceptionEnum.REDIS_ERROR.getCode());
            }
        }

        private void parseEntity(Class<T> entityClass){
            if(entityClass.isAnnotationPresent(RedisEntity.class)){
                RedisEntity annotation = entityClass.getAnnotation(RedisEntity.class);
                String entityName = annotation.name();
                if(entityName.equals("")){
                    entityName = entityClass.getName();
                }

                if(keyPrefix == null){
                    keyPrefix = entityName;
                }
            }

            Field[] fields = entityClass.getDeclaredFields();
            propNameToRedisFieldName = new HashMap<String, String>();
            redisFieldNameToField = new HashMap<String, Field>();

            if(fields!=null){
                for(Field field:fields){
                    if(field.isAnnotationPresent(RedisField.class)){
                        RedisField annotation = field.getAnnotation(RedisField.class);
                        //先找注解name属性值,如果没有,就使用注解属性名
                        String redisFieldName = annotation.name();
                        if(redisFieldName.equals("")){
                            redisFieldName = field.getName();
                        }

                        field.setAccessible(true);
                        propNameToRedisFieldName.put(field.getName().toLowerCase(), redisFieldName.toLowerCase());
                        redisFieldNameToField.put(redisFieldName.toLowerCase(), field);

                        if (annotation.inUniqueKey()){
                            if (uniqueIndexRedisFields == null){
                                uniqueIndexRedisFields = new TreeMap<>();
                            }
                            uniqueIndexRedisFields.put(redisFieldName, field);
                        }

                    } else if(field.isAnnotationPresent(RedisId.class)){//主键不存储
                        field.setAccessible(true);
                        idRedisField = field;
                    }
                }
            }
        }

        private String getIndexType(){
            return "s:" + keyPrefix;
        }

        public HashCacheRao getHashCacheRao() {
            return hashCacheRao;
        }

        public void setHashCacheRao(HashCacheRao hashCacheRao) {
            this.hashCacheRao = hashCacheRao;
        }

        public Integer getSeconds() {
            return seconds;
        }

        public void setSeconds(Integer seconds) {
            this.seconds = seconds;
        }

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        public StringCacheRao getStringCacheRao() {
            return stringCacheRao;
        }

        public void setStringCacheRao(StringCacheRao stringCacheRao) {
            this.stringCacheRao = stringCacheRao;
        }

        public KeysRao getKeysRao() {
            return keysRao;
        }

        public void setKeysRao(KeysRao keysRao) {
            this.keysRao = keysRao;
        }
}
