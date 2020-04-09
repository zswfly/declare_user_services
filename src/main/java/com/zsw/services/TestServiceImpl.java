package com.zsw.services;

import com.zsw.dao.UserMapper;
import com.zsw.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangshaowei on 2020/4/2.
 */
@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User isUser(String loginName, String loginPWD) {
        return userMapper.getUser(loginName,loginPWD);
    }
}
