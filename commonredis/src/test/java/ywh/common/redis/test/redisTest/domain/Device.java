package ywh.common.redis.test.redisTest.domain;

import ywh.common.redis.annotation.RedisField;
import ywh.common.redis.annotation.RedisId;

import java.io.Serializable;

public class Device implements Serializable {

    @RedisId
    private Long id;

    @RedisField
    private String name;

    @RedisField
    private Boolean openStatus;

    @RedisField(inUniqueKey = true)           //device name
    private String sn;

    @RedisField
    private Integer deviceType;

    private long startTime ;

    @RedisField(inUniqueKey = true)
    private String gateWay;

    private Long updateTime;

    public Device(){}

    public Device(String name, long startTime, Integer type, String sn, String gateWay){
        this.name = name;
        this.startTime = startTime;
        this.deviceType = type;
        this.sn = sn;
        this.gateWay = gateWay;
        this.updateTime = 1l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Boolean openStatus) {
        this.openStatus = openStatus;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "name: " + this.name + " type: " + this.deviceType.toString() + " sn: " + this.sn +
                " gateWay: " + this.gateWay;
    }
}
