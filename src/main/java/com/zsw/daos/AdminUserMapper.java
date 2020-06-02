package com.zsw.daos;

import com.zsw.entitys.user.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AdminUserMapper {

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);
}