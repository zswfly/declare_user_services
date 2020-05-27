package com.zsw.services;

import com.zsw.entitys.AdminUserEntity;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
public interface IAdminUserService extends IBaseService{
    AdminUserEntity getAdminUser(AdminUserEntity param) throws Exception;
    void updateRememberToken(Integer adminUserId,String rememberToken)throws Exception;

}
