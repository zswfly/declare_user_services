package com.zsw.services;

import com.zsw.daos.AdminUserMapper;
import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.AdminUserEntity;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.UserServiceStaticWord;
import org.apache.commons.lang3.StringUtils;
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
public class AdminUserServiceImpl implements IAdminUserService,Serializable {


    private static final long serialVersionUID = 758431059117830918L;

    @Autowired
    private IDBService dbService;

    @Resource
    private AdminUserMapper adminUserMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public AdminUserEntity getAdminUser(AdminUserEntity param) throws Exception {
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void newAdminUser(UserDto userDto, Integer currentUserId) throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);
        userEntity.setId(null);
        userEntity.setStatus(CommonStaticWord.Normal_Status_0);
        userEntity.setCreateUser(currentUserId);
        userEntity.setCreateTime(new Timestamp(new Date().getTime()));
        userEntity.setUpdateUser(currentUserId);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(userEntity);


        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(userEntity.getId());
        adminUserEntity.setStatus(CommonStaticWord.Normal_Status_0);
        adminUserEntity.setCreateUser(currentUserId);
        adminUserEntity.setCreateTime(new Timestamp(new Date().getTime()));
        adminUserEntity.setUpdateUser(currentUserId);
        adminUserEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(adminUserEntity);

    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception {
        int status = CommonStaticWord.Status_ban.equals(type)?CommonStaticWord.Ban_Status_1:CommonStaticWord.Normal_Status_0 ;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId",currentUserId);
        paramMap.put("status",status);
        paramMap.put("ids",ids);
        this.adminUserMapper.batchBan(paramMap);
    }
}
