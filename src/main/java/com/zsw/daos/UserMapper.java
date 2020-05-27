package com.zsw.daos;

import com.zsw.entitys.user.UserDto;
import com.zsw.entitys.user.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UserMapper {

    List<UserDto> usersPage(@Param("paramMap") Map<String,Object> paramMap);
    Integer usersPageCount(@Param("paramMap") Map<String,Object> paramMap);
    Map<String,String> checkUserExist(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap") Map<String,Object> paramMap);

}