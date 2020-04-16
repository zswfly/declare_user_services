package com.zsw.services;

import com.zsw.entitys.UserEntity;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
public interface IUserService  extends IBaseService{

    UserEntity getUser(UserEntity param) throws Exception;
}
