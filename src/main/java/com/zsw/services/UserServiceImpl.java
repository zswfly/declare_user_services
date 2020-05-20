package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.UserServiceStaticWord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

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

    @Resource
    private CompanyMapper companyMapper;

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
    public UserEntity newUser(UserDto userDto, Integer currentUserId)throws Exception {
        userDto.setId(null);
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);
        userEntity.setStatus(0);
        userEntity.setCreateUser(currentUserId);
        userEntity.setCreateTime(new Timestamp(new Date().getTime()));
        userEntity.setUpdateUser(currentUserId);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(userEntity);
        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity updateUser(UserDto userDto, Integer currentUserId) throws Exception{
        userDto.setLoginPwd(null);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());

        userEntity = this.dbService.get(userEntity);

        if(userDto.getStatus() == CommonStaticWord.Ban_Status_1){
            List<Integer> userIds = new ArrayList<>();
            userIds.add(userDto.getId());
            Map<String, Object > param = new HashMap<>();
            param.put("userIds",userIds);
            List<Integer> managerIds = this.companyMapper.checkCompanyManagerIds(param);
            if((managerIds!=null && managerIds.contains(userDto.getId()))
                    || userDto.getId() == currentUserId
                    )userDto.setStatus(userEntity.getStatus());
            //不能禁用manager用户 , 当前用户自己
        }

        userDto.setLoginPwd(userEntity.getLoginPwd());

        BeanUtils.copyProperties(userDto,userEntity);

        userEntity.setUpdateUser(currentUserId);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception{
        int falg =0;
        List<Integer> managerIds = null;
        if( UserServiceStaticWord.User_Status_ban.equals(type)){
            falg = 1;
            Map<String, Object > param = new HashMap<>();
            param.put("userIds",ids);
            managerIds = this.companyMapper.checkCompanyManagerIds(param);

        }

        List<UserEntity> list = this.dbService.findBy(UserEntity.class,"id",ids);

        for (UserEntity item : list){
            if(item.getId() != currentUserId
                    && (managerIds != null && !managerIds.contains(item.getId()))){
                item.setStatus(falg);
                item.setUpdateUser(currentUserId);
                item.setUpdateTime(new Timestamp(new Date().getTime()));
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
