package com.zsw.services;

import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
public interface IRoleService extends IBaseService{
    RoleEntity newRole(RoleEntity roleEntity, Integer currentUserId,Integer currentCompanyId)throws Exception;

    RoleEntity updateRole(RoleEntity roleEntity, Integer currentUserId,Integer currentCompanyId) throws Exception;

    RoleEntity getRole(RoleEntity param) throws Exception;

    List<RoleEntity> listRoleEntity(Map<String, Object> paramMap) throws Exception;

    Integer listRoleEntityCount(Map<String, Object> paramMap) throws Exception;

    String checkRoleExist(RoleEntity roleEntity,Integer currentCompanyId) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId,Integer currentCompanyId) throws Exception;

    void relationOrDeleteRolePermission(List<Integer> permissionIds, Integer roleId, Integer currentUserId,Integer currentCompanyId,Boolean isDelete) throws Exception;

    List<PermissionEntity> getRolePermissions(Map<String, Object> paramMap) throws Exception;



}
