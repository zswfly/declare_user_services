package com.zsw.services;

import org.springframework.stereotype.Service;

/**
 * Created by zhangshaowei on 2020/4/2.
 */
@Service
public class TestServiceImpl implements TestService{
    @Override
    public boolean isUser(String userName, String passWord) {
        return true;
    }
}
