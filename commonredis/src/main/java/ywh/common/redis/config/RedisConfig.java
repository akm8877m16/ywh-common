package ywh.common.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "ywh.redis")
public class RedisConfig {

    public static final String PREFIX = "ywh.redis";
    //redisson
    private String address;

    private String password = "moreton123";
    private int database=15;
    private ClusterServer clusterServer;
    private String codec = "org.redisson.codec.JsonJacksonCodec";
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public ClusterServer getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(ClusterServer clusterServer) {
        this.clusterServer = clusterServer;
    }

    public static class ClusterServer{

        private String[] nodeAddresses;

        public String[] getNodeAddresses() {
            return nodeAddresses;
        }

        public void setNodeAddresses(String[] nodeAddresses) {
            this.nodeAddresses = nodeAddresses;
        }
    }
}