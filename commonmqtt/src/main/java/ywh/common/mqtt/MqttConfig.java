package ywh.common.mqtt;

public interface  MqttConfig {

    /**
     * Custom Configuration
     *
     * @param broker
     * @param port
     * @param withUserNamePass
     */
     void config(String broker, Integer port, Boolean withUserNamePass);

    /**
     * Default Configuration
     */
      void config();


}