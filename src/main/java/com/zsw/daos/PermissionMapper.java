package com.zsw.daos;

import com.zsw.entitys.user.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface PermissionMapper {

    List<UserPermission> listUserPermission( @Param("ids") List<Integer> ids);
}