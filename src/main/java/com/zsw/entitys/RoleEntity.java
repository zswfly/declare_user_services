package com.zsw.entitys;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/6/2.
 */
@Entity
@Table(name = "role", schema = "user")
public class RoleEntity extends IDEntity {
    private Integer id;
    private String name;
    private String description;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;
    private Integer companyId;
    private Integer status;


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
    //@Column(name = "name", nullable = false, length = 80)
    @NotNull
    @Length(max = 80)
    @Column(name = "name", unique = false,  nullable = false, insertable = true, updatable = true, length = 80)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    //@Column(name = "description", nullable = true, length = 255)
    @Length(max = 255)
    @Column(name = "description", unique = false,  nullable = true, insertable = true, updatable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Basic
    @Max(9999999999L)
    @Column(name = "company_id", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

        RoleEntity that = (RoleEntity) o;

        if (id != that.id) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (companyId != that.companyId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        result = 31 * result + companyId;
        return result;
    }
}
