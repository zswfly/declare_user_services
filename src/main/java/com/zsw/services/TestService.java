package com.zsw.services;

import com.zsw.entitys.User;

/**
 * Created by zhangshaowei on 2020/4/2.
 */
public interface TestService {
    User isUser(String loginName, String loginPSW);
}
