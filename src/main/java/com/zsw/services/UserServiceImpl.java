package com.zsw.services;

import com.zsw.daos.UserMapper;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.UserServiceStaticWord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
@Service
public class UserServiceImpl implements IUserService,Serializable{

    private static final long serialVersionUID = -4721228136111002555L;

    @Autowired
    private IDBService dbService;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void updateRememberToken(Integer userId, String rememberToken) throws Exception {
        UserEntity userEntity = this.dbService.get(UserEntity.class,userId);
        userEntity.setRememberToken(rememberToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserEntity getUser(UserEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity resetPassWord(UserEntity param,String resetPassWord) throws Exception {
        UserEntity entity = this.dbService.get(param);
        if (entity != null) {
            entity.setLoginPwd(resetPassWord);
            entity.setUpdateUser(entity.getId());
            entity.setUpdateTime(new Timestamp(new Date().getTime()));
        }
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity newUser(UserDto userDto)throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);
        userEntity.setStatus(0);
        userEntity.setCreateUser(1);
        userEntity.setCreateTime(new Timestamp(new Date().getTime()));
        userEntity.setUpdateUser(1);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(userEntity);
        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity updateUser(UserDto userDto) throws Exception{
        userDto.setLoginPwd(null);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());

        userEntity = this.dbService.get(userEntity);
        userDto.setLoginPwd(userEntity.getLoginPwd());
        BeanUtils.copyProperties(userDto,userEntity);

        userEntity.setUpdateUser(userDto.getId());
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception{
        int falg = UserServiceStaticWord.User_Status_ban.equals(type)?1:0;
        List<UserEntity> list = this.dbService.findBy(UserEntity.class,"id",ids);
        for (UserEntity item : list){
            if(item.getId() != currentUserId){
                item.setStatus(falg);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserEntity> getUsersByIds(List<Integer> ids) throws Exception{
        return this.dbService.findBy(UserEntity.class,"id",ids);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserDto> usersPage(Map<String, Object> paramMap) throws Exception{
        return this.userMapper.usersPage(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer usersPageCount(Map<String, Object> paramMap) throws Exception {
        return this.userMapper.usersPageCount(paramMap);
    }


}
