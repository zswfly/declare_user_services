package com.zsw.services;

import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/13.
 */
public interface IPermissionService extends IBaseService {

    public Integer initPermission(List<InitPermission> list) throws Exception;

    public List<UserPermission> listUserPermission(List<Integer> ids) throws Exception;

    Integer listPermissionEntityCount( Map<String,Object> paramMap)throws Exception;
    List<PermissionEntity> listPermissionEntity(Map<String,Object> paramMap)throws Exception;
    List<PermissionEntity> listPermissionEntity2(Map<String,Object> paramMap)throws Exception;

    PermissionEntity getPermission(PermissionEntity param)throws Exception;


}
