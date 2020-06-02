package com.zsw.services;

import com.zsw.entitys.AdminUserEntity;
import com.zsw.entitys.user.UserDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
public interface IAdminUserService extends IBaseService{
    AdminUserEntity getAdminUser(AdminUserEntity param) throws Exception;

    void newAdminUser(UserDto userDto, Integer currentUserId) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;


}
