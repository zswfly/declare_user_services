package com.zsw.services;

import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;

import java.util.List;

/**
 * Created by zhangshaowei on 2020/4/13.
 */
public interface IPermissionService extends IBaseService {

    public Integer initPermission(List<InitPermission> list) throws Exception;

    public List<UserPermission> listUserPermission(List<Integer> ids) throws Exception;
}
