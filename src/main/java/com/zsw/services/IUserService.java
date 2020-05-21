package com.zsw.services;

import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.UserDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
public interface IUserService  extends IBaseService{

    UserEntity getUser(UserEntity param) throws Exception;
    UserEntity resetPassWord(UserEntity param,String resetPassWord) throws Exception;
    void newUser(UserDto userDto, Integer currentUserId)throws Exception;
    UserEntity updateUser(UserDto userDto, Integer currentUserId) throws Exception;
    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;
    List<UserEntity> getUsersByIds(List<Integer> ids) throws Exception;
    List<UserDto> usersPage(Map<String, Object> paramMap) throws Exception;
    Integer usersPageCount(Map<String, Object> paramMap) throws Exception;
    void updateRememberToken(Integer userId,String rememberToken)throws Exception;
}
