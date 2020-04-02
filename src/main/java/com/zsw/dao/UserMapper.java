package com.zsw.dao;

import com.zsw.entity.User;
import java.util.List;

public interface UserMapper {
    int insert(User record);

    List<User> selectAll();
}