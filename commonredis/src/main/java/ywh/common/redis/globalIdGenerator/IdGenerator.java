package ywh.common.redis.globalIdGenerator;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLongAdder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    @Autowired
    protected RedissonClient redissonClient;

    private String idName;


    /*
    public RLongAdder getIdGenerator(String name){
        RLongAdder atomicLong = redissonClient.getLongAdder(idName);
        return  atomicLong;
    }
    */

    public Long generatNewId(){
        RAtomicLong atomicLong = redissonClient.getAtomicLong(idName);
        return atomicLong.incrementAndGet();
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

}
