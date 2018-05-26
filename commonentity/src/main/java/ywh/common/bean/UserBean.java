package ywh.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="user对象",description="用户对象user")
public class UserBean {

    @ApiModelProperty(value="用户名",name="userName",example="mike",required = true)
    private String userName;

    @ApiModelProperty(value="密码",name="passWord",example="12344*sdf",required = true)
    private String passWord;

    @ApiModelProperty(value="是否禁用 1 用户可用 0 用户禁用",name="enabled",example="1")
    private Boolean enabled;

    @ApiModelProperty(value="手机号",name="mobilePhone",example="12345678909")
    private String mobilePhone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}

