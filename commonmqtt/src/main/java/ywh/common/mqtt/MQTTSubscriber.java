package ywh.common.mqtt;

public interface MQTTSubscriber {
    /**
     * Subscribe message
     *
     * @param topic
     */
    public void subscribeTopic(String topic);

    /**
     *  unsubscribeTopic
     *
     * @param topic
     */
    public void unsubscribeTopic(String topic);

    /**
     * Disconnect MQTT Client
     */
    public void disconnect();
}
