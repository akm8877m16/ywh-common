package ywh.common.mqtt;

public interface MQTTPublisher {

    /**
     * Publish message
     *
     * @param topic
     * @param message
     */
     void publishMessage(String topic, String message);

    /**
     * Disconnect MQTT Client
     */
     void disconnect();

}
