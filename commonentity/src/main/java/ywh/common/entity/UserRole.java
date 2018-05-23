package ywh.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="authorities",uniqueConstraints=@UniqueConstraint(
        columnNames={"username","authority"}))
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",nullable=false,referencedColumnName = "username")
    @JsonBackReference
    private User user;

    @Column(nullable=false,length=45)
    private String authority;

    public UserRole(){}

    public UserRole(String authority){
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user=user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


}
