package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.RoleMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.DepartmentUserEntity;
import com.zsw.entitys.RoleEntity;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.PinyinUtils;
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
public class RoleServiceImpl implements IRoleService,Serializable {

    private static final long serialVersionUID = -3936095524540652904L;

    @Autowired
    private IDBService dbService;

    @Resource
    private RoleMapper roleMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newRole(RoleEntity roleEntity, Integer currentUserId,Integer currentCompanyId)throws Exception {
        roleEntity.setId(null);
        roleEntity.setCompanyId(currentCompanyId);
        roleEntity.setStatus(CommonStaticWord.Normal_Status_0);
        roleEntity.setCreateUser(currentUserId);
        roleEntity.setCreateTime(new Timestamp(new Date().getTime()));
        roleEntity.setUpdateUser(currentUserId);
        roleEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(roleEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public RoleEntity updateRole(RoleEntity roleEntity, Integer currentUserId,Integer currentCompanyId) throws Exception{
        if(roleEntity == null
                || roleEntity.getId() == null
                ){
            throw new Exception("参数错误");
        }
        RoleEntity param = new RoleEntity();
        param.setId(roleEntity.getId());
        param.setCompanyId(currentCompanyId);
        RoleEntity result = this.dbService.get(param);

        if(result == null) throw new Exception("公司没有该角色id");

        BeanUtils.copyProperties(roleEntity,result);

        result.setUpdateUser(currentUserId);
        result.setUpdateTime(new Timestamp(new Date().getTime()));

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public RoleEntity getRole(RoleEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<RoleEntity>  listRoleEntity(Map<String, Object> paramMap) throws Exception{
        return this.roleMapper.listRoleEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listRoleEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.roleMapper.listRoleEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkRoleExist(RoleEntity roleEntity,Integer currentCompanyId) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(roleEntity.getId() == null){

            RoleEntity param = new RoleEntity();

            param.setName(roleEntity.getName());
            param.setCompanyId(currentCompanyId);
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("角色名已存在");

        }else{
            RoleEntity param = new RoleEntity();
            List<RoleEntity> resultList = null;
            param.setName(roleEntity.getName());
            param.setCompanyId(currentCompanyId);
            resultList = this.dbService.find(param);
            for(RoleEntity result :resultList){
                result = this.dbService.get(param);
                if( result != null && result.getId() != roleEntity.getId() ) {
                    stringBuilder.append("角色名已存在");
                    break;
                }
            }
        }

        return stringBuilder.toString();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId,Integer currentCompanyId) throws Exception{
        int status = CommonStaticWord.Status_ban.equals(type)?CommonStaticWord.Ban_Status_1:CommonStaticWord.Normal_Status_0 ;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId",currentUserId);
        paramMap.put("currentCompanyId",currentCompanyId);
        paramMap.put("status",status);
        paramMap.put("ids",ids);
        this.roleMapper.batchBan(paramMap);

    }





}