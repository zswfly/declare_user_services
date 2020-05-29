package com.zsw.entitys;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/4/24.
 */
@Entity
@Table(name = "admin_user", schema = "user")
public class AdminUserEntity extends IDEntity {


    private Integer userId;
    private Integer status;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;
    private String rememberToken;

    @Id
    @Column(name="user_id", length=11, nullable=false, unique=true, insertable=true, updatable=false)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminUserEntity that = (AdminUserEntity) o;

        if (userId != that.userId) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (rememberToken != null ? !rememberToken.equals(that.rememberToken) : that.rememberToken != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = userId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        result = 31 * result + (rememberToken != null ? rememberToken.hashCode() : 0);
        return result;
    }
}
