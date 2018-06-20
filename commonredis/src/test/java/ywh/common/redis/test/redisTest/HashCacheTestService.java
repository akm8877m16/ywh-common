package ywh.common.redis.test.redisTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.JsonJacksonMapCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ywh.common.redis.HashCacheRao;
import ywh.common.redis.impl.HashCacheRaoImpl;
import ywh.common.redis.test.YwhCommonRedisTestApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YwhCommonRedisTestApplication.class)
public class HashCacheTestService {

    @Autowired
    private HashCacheRaoImpl hashCacheRaoImpl;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("a","123");
        map.put("b","123asfasf");
        hashCacheRaoImpl.hmset("test1","test",map);
        RMap<String,String> map1 = redissonClient.getMap("test:{test1}");
        assertThat(map1.get("a")).isEqualTo("123");
    }

}
