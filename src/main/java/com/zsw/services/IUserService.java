package com.zsw.services;

import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.LoginTemp;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
public interface IUserService  extends IBaseService{

    UserEntity getUser(UserEntity param) throws Exception;
    UserEntity resetPassWord(UserEntity param,String resetPassWord) throws Exception;
}
