package com.zsw.services;

import com.zsw.daos.UserMapper;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.LoginTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
@Service
public class UserServiceImpl implements IUserService,Serializable{

    private static final long serialVersionUID = -4721228136111002555L;

    @Autowired
    private IDBService dbService;

    @Resource
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserEntity getUser(UserEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    public UserEntity resetPassWord(UserEntity param,String resetPassWord) throws Exception {
        UserEntity entity = this.dbService.get(param);
        if (entity != null) entity.setLoginPwd(resetPassWord);
        return entity;
    }


}
