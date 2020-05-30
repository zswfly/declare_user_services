package com.zsw.entitys;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/4/24.
 */
@Entity
@Table(name = "user", schema = "user")
public class UserEntity extends IDEntity {


    private Integer id;
    private String userName;
    private String loginPwd;
    private Integer status;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;
    private String phone;
    private String email;
    private String avatar;
    private String rememberToken;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", length=11, nullable=false, unique=true, insertable=true, updatable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @NotNull
    @Length(max = 80)
    @Column(name = "user_name", unique = false,  nullable = false, insertable = true, updatable = true, length = 80)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    //@Column(name = "login_pwd", nullable = false, length = 32)
    @NotNull
    @Length(max = 100)
    @Column(name = "login_pwd", unique = false,  nullable = false, insertable = true, updatable = true, length = 100)
    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    @Basic
    //@Column(name = "status", nullable = true)
    @Length(max = 3)
    @Column(name = "status", unique = false,  nullable = true, insertable = true, updatable = true, length = 3)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    //@Column(name = "create_time", nullable = false)
    @Column(name = "create_time", nullable = true, unique = false, insertable = true, updatable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    //@Column(name = "create_user", nullable = false)
    @Column(name = "create_user", length = 11, nullable = true, unique = false, insertable = true, updatable = true)
    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    @Basic
    //@Column(name = "update_time", nullable = false)
    @Column(name = "update_time", nullable = true, unique = false, insertable = true, updatable = false)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    //@Column(name = "update_user", nullable = false)
    @Column(name = "update_user", length = 11, nullable = true, unique = false, insertable = true, updatable = true)
    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    @Basic
    //@Column(name = "phone", nullable = false, length = 20)
    @NotNull
    @Length(max = 30)
    @Column(name = "phone", unique = true,  nullable = false, insertable = true, updatable = true, length = 30)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    //@Column(name = "email", nullable = false, length = 20)
    @NotNull
    @Length(max = 70)
    @Column(name = "email", unique = true,  nullable = false, insertable = true, updatable = true, length = 70)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    //@Column(name = "avatar", nullable = true, length = 50)
    @Length(max = 50)
    @Column(name = "avatar", unique = false,  nullable = true, insertable = true, updatable = true, length = 50)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    //@Column(name = "remember_token", nullable = true, length = 100)
    @Length(max = 100)
    @Column(name = "remember_token", unique = false,  nullable = true, insertable = true, updatable = true, length = 100)
    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (loginPwd != null ? !loginPwd.equals(that.loginPwd) : that.loginPwd != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) return false;
        if (rememberToken != null ? !rememberToken.equals(that.rememberToken) : that.rememberToken != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (loginPwd != null ? loginPwd.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (rememberToken != null ? rememberToken.hashCode() : 0);
        return result;
    }
}
