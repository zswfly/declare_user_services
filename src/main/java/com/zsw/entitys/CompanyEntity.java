package com.zsw.entitys;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
@Entity
@Table(name = "company", schema = "user")
public class CompanyEntity extends IDEntity{
    private Integer id;
    private String name;
    private Integer status;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;
    private String url;
    private String mnemonicCode;
    private String contact;
    private String contactPhone;
    private String contactAddress;
    private Integer size;
    private Integer creatorId;
    private Date contractStartAt;
    private Date contractEndAt;
    private Integer managerId;

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
    @Length(max = 100)
    @Column(name = "url", unique = false,  nullable = true, insertable = true, updatable = true, length = 100)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
//
//    @Basic
//    @Column(name = "mnemonic_code", nullable = false, length = 255)
    @Basic
    @Length(max = 255)
    @Column(name = "mnemonic_code", unique = false,  nullable = true, insertable = true, updatable = true, length = 255)
    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

//    @Basic
//    @Column(name = "contact", nullable = false, length = 255)
    @Basic
    @NotNull
    @Length(max = 255)
    @Column(name = "contact", unique = false,  nullable = false, insertable = true, updatable = true, length = 255)
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

//    @Basic
//    @Column(name = "contact_phone", nullable = false, length = 255)
    @Basic
    @NotNull
    @Length(max = 255)
    @Column(name = "contact_phone", unique = false,  nullable = false, insertable = true, updatable = true, length = 255)
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

//    @Basic
//    @Column(name = "contact_address", nullable = false, length = 255)
    @Basic
    @NotNull
    @Length(max = 255)
    @Column(name = "contact_address", unique = false,  nullable = false, insertable = true, updatable = true, length = 255)
    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

//    @Basic
//    @Column(name = "size", nullable = false)
    @Basic
    @NotNull
    @Column(name = "size", unique = false,  nullable = false, insertable = true, updatable = true, length = 11)
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

//    @Basic
//    @Column(name = "creator_id", nullable = false)
    @Basic
    @Max(9999999999L)
    @Column(name = "creator_id", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }


    @Basic
    @Max(9999999999L)
    @Column(name = "manager_id", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    //    @Basic
//    @Column(name = "contract_start_at", nullable = true)
    @Basic
    @Column(name = "contract_start_at", nullable = true, unique = false, insertable = true, updatable = true)
    public Date getContractStartAt() {
        return contractStartAt;
    }

    public void setContractStartAt(Date contractStartAt) {
        this.contractStartAt = contractStartAt;
    }

//    @Basic
//    @Column(name = "contract_end_at", nullable = true)
    @Basic
    @Column(name = "contract_end_at", nullable = true, unique = false, insertable = true, updatable = true)
    public Date getContractEndAt() {
        return contractEndAt;
    }

    public void setContractEndAt(Date contractEndAt) {
        this.contractEndAt = contractEndAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEntity that = (CompanyEntity) o;

        if (id != that.id) return false;
        if (status != that.status) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (size != that.size) return false;
        if (creatorId != that.creatorId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (mnemonicCode != null ? !mnemonicCode.equals(that.mnemonicCode) : that.mnemonicCode != null) return false;
        if (contact != null ? !contact.equals(that.contact) : that.contact != null) return false;
        if (contactPhone != null ? !contactPhone.equals(that.contactPhone) : that.contactPhone != null) return false;
        if (contactAddress != null ? !contactAddress.equals(that.contactAddress) : that.contactAddress != null)
            return false;
        if (contractStartAt != null ? !contractStartAt.equals(that.contractStartAt) : that.contractStartAt != null)
            return false;
        if (contractEndAt != null ? !contractEndAt.equals(that.contractEndAt) : that.contractEndAt != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (mnemonicCode != null ? mnemonicCode.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (contactPhone != null ? contactPhone.hashCode() : 0);
        result = 31 * result + (contactAddress != null ? contactAddress.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + creatorId;
        result = 31 * result + (contractStartAt != null ? contractStartAt.hashCode() : 0);
        result = 31 * result + (contractEndAt != null ? contractEndAt.hashCode() : 0);
        return result;
    }
}
