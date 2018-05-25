package ywh.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="devices")
public class Device implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="sn",unique=true,nullable = false)
    private String sn;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",nullable = false,referencedColumnName = "username")
    @JsonBackReference
    private User user;

    public Device(){}

    public Device(String sn){
        this.sn = sn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
