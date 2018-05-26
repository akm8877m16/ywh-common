package ywh.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="角色对象",description="角色对象role")
public class RoleBean {

    @ApiModelProperty(value="用户名",name="userName",example="mike",required = true)
    private String userName;

    @ApiModelProperty(value="角色，目前只有两个:ROLE_USER,ROLE_ADMIN",name="userName",example="ROLE_USER",required = true)
    private String role;

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
