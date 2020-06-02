package com.zsw.entitys;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/6/2.
 */
@Entity
@Table(name = "role_permission", schema = "user")
public class RolePermissionEntity extends IDEntity {
    private Integer roleId;
    private Integer permissionId;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;

    @Id
    @Column(name="role_id", length=11, nullable=true, unique=false, insertable=true, updatable=false)
    //@Column(name = "role_id", nullable = false)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Id
    @Column(name="permission_id", length=11, nullable=true, unique=false, insertable=true, updatable=false)
    //@Column(name = "permission_id", nullable = false)
    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
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
    @Column(name = "create_user", length = 11, nullable = true, unique = false, insertable = true, updatable = false)
    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    @Basic
    //@Column(name = "update_time", nullable = false)
    @Column(name = "update_time", nullable = true, unique = false, insertable = true, updatable = true)
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

        RolePermissionEntity that = (RolePermissionEntity) o;

        if (roleId != that.roleId) return false;
        if (permissionId != that.permissionId) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = roleId;
        result = 31 * result + permissionId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        return result;
    }
}
