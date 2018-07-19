package ywh.common.redis.test.redisTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ywh.common.redis.impl.HashCacheRaoImpl;
import ywh.common.redis.test.YwhCommonRedisTestApplication;
import ywh.common.redis.test.redisTest.domain.Device;
import ywh.common.redis.test.redisTest.raoImpl.DeviceRaoImpl;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = YwhCommonRedisTestApplication.class)
public class DeviceRaoImplTest {

    private static final Logger logger = LoggerFactory.getLogger(DeviceRaoImplTest.class);

    @Resource
    private DeviceRaoImpl deviceRao;

    @Test
    public void addDevice() {
        Device device1 = new Device("test", 111111L, 3, "asfd2sdfa", "asdfw9099");
        device1.setOpenStatus(true);
        device1.setUpdateTime(123131231L);
        device1.setId(1l);

        Device device2 = new Device("test", 111111L, 3, "asfad2sdfa", "asdfw9099");
        device2.setOpenStatus(true);
        device2.setUpdateTime(123131231L);
        device2.setId(2l);

        Device device3 = new Device("test", 111111L, 3, "asf0d2sdfa", "asdfw9099");
        device3.setOpenStatus(true);
        device3.setUpdateTime(123131231L);
        device3.setId(3l);

        deviceRao.add(device1);
        deviceRao.add(device2);
        deviceRao.add(device3);

        Device result = deviceRao.get(1l);
        logger.info(result.toString());
        System.out.println(result.toString());

        result = deviceRao.getUnique(device2);
        logger.info(result.toString());
        System.out.println(result.toString());

        Device searchDevice = new Device();
        searchDevice.setGateWay("asdfw9099");
        searchDevice.setSn("asf0d2sdfa");
        result = deviceRao.getUnique(searchDevice);
        logger.info(result.toString());
        System.out.println(result.toString());

        //find by gateway
        List<Device> deviceList = deviceRao.findByKeyPattern("*asdfw9099*");
        for(Device device : deviceList){
            System.out.println(device.toString());
        }

    }

    @Test
    public void updateDevice(){
        Device device2 = new Device("testupdate", 111111L, 3, "asfad2sdfa", "asdfw9099");
        device2.setOpenStatus(false);
        device2.setUpdateTime(123131231L);
        device2.setId(2l);
        deviceRao.update(device2);
        Device result = deviceRao.getUnique(device2);
        System.out.println(result.toString());
    }



}
