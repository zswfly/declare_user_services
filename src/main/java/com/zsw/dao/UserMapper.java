package com.zsw.dao;

import com.zsw.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    int insert(User record);

    List<User> selectAll();

    User getUser(@Param("userName") String userName, @Param("passWord")String passWord);
}