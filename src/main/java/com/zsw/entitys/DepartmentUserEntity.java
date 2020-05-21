package com.zsw.entitys;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/5/21.
 */
@Entity
@Table(name = "department_user", schema = "user")
//@IdClass(DepartmentUserEntityPK.class)
public class DepartmentUserEntity extends IDEntity {
    private Integer departmentId;
    private Integer userId;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;

    @Id
//    @Column(name = "department_id", nullable = false)
    @Column(name="department_id", length=11, nullable=true, unique=false, insertable=true, updatable=false)
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Id
    @Column(name="user_id", length=11, nullable=true, unique=false, insertable=true, updatable=false)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
//    @NotNull
//    @Min(1L)
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

        DepartmentUserEntity that = (DepartmentUserEntity) o;

        if (departmentId != that.departmentId) return false;
        if (userId != that.userId) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = departmentId;
        result = 31 * result + userId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        return result;
    }
}
