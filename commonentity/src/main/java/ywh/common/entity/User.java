package ywh.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ywh.common.bean.RoleBean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username",unique=true,nullable=false,length=45)
    private String username;

    @Column(name="password",nullable=false,length=100)
    private String password;

    @Column(name="enabled",nullable=false)
    private boolean enabled;

    @Column()
    private String moilbePhone;

    public String getMoilbePhone() {
        return moilbePhone;
    }

    public void setMoilbePhone(String moilbePhone) {
        this.moilbePhone = moilbePhone;
    }

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<UserRole> userRoles=new HashSet<UserRole>(0);

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Device> userDevices = new HashSet<Device>(0);

    public User(){}

    public User(String username, String password){
        this(username,password,true);
    }

    public User(String username,String password,boolean enabled){
        this.username=username;
        this.password=password;
        this.enabled=enabled;
    }

    public User(String username,String password,boolean enabled,Set<UserRole> userRole){
        this.username=username;
        this.password=password;
        this.enabled=enabled;
        this.userRoles=userRole;
    }

    public void addRole(UserRole userRole){
        userRole.setUser(this);
        userRoles.add(userRole);
    }

    public void addDevice(Device device){
        device.setUser(this);
        userDevices.add(device);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled=enabled;
    }

    public Set<UserRole> getUserRole(){
        return this.userRoles;
    }

    public void setUserRole(Set<UserRole> userRole){
        this.userRoles=userRole;
    }

    public Boolean isRoleExist(RoleBean roleBean){
        for(UserRole userRole : userRoles){
            if(userRole.getAuthority().equals(roleBean.getRole()) && username.equals(roleBean.getUsername())){
                return true;
            }
        }
        return false;
    }

    public Boolean isDeviceExist(Device device){
        for(Device device1 : userDevices){
            if(device1.getSn().equals(device.getSn())){
                return true;
            }
        }
        return false;
    }
}

