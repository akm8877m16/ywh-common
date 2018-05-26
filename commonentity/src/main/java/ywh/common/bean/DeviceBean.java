package ywh.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="设备对象",description="设备对象device")
public class DeviceBean{

    @ApiModelProperty(value="用户名",name="userName",example="mike",required = true)
    private String userName;

    @ApiModelProperty(value="设备唯一标识sn",name="sn",example="123dsfsdwer123",required = true)
    private String sn;

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
