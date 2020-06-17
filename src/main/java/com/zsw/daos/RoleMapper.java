package com.zsw.daos;

import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RoleMapper {
    Integer listRoleEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<RoleEntity> listRoleEntity(@Param("paramMap") Map<String,Object> paramMap);
    List<RoleEntity> listRoleEntity2(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);


}