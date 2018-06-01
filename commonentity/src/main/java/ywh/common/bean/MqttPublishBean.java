package ywh.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="MQTT发布对象",description="用于MQTT发布")
public class MqttPublishBean {

    @ApiModelProperty(value="Mqtt发布Topic属性",name="attribute",required = true)
    private String attribute;
    @ApiModelProperty(value="Mqtt发布指令报文",name="payload",required = true)
    private String payload;
    @ApiModelProperty(value="Mqtt发布指令报文",name="sn",required = true)
    private String sn;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
