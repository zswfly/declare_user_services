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
public class AdminUserServiceImpl implements IAdminUserService,Serializable{


    private static final long serialVersionUID = 758431059117830918L;

    @Autowired
    private IDBService dbService;

    @Resource
    private AdminUserMapper adminUserMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public AdminUserEntity getAdminUser(AdminUserEntity param) throws Exception {
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void updateRememberToken(Integer adminUserId, String rememberToken) throws Exception {
        AdminUserEntity adminUserEntity = this.dbService.get(AdminUserEntity.class,adminUserId);
        adminUserEntity.setRememberToken(rememberToken);
    }
}
