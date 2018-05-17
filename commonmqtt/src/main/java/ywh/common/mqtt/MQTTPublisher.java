package ywh.common.mqtt;

public interface MQTTPublisher {

    /**
     * Publish message
     *
     * @param topic
     * @param message
     */
    public void publishMessage(String topic, String message);

    /**
     * Disconnect MQTT Client
     */
    public void disconnect();

}
