package com.zsw.daos;

import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.user.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PermissionMapper {

    List<UserPermission> listUserPermission( @Param("ids") List<Integer> ids);
    List<PermissionEntity> listPermissionEntity(@Param("paramMap") Map<String,Object> paramMap);
    Integer listPermissionEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<PermissionEntity> getRolePermissions(@Param("paramMap") Map<String,Object> paramMap);




}