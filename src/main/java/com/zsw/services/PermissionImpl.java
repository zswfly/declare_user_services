package com.zsw.services;

import com.zsw.daos.PermissionMapper;
import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;
import com.zsw.utils.CommonStaticWord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangshaowei on 2020/4/13.
 */
@Service
public class PermissionImpl extends BaseServiceImpl implements IPermissionService ,Serializable {

    private static final long serialVersionUID = 3848006822055456514L;

    @Autowired
    private IDBService dbService;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 数据库更新权限
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public Integer  initPermission(List<InitPermission> listVO) throws Exception {

        List<PermissionEntity> listEntity = this.dbService.getAll(PermissionEntity.class);
        List<PermissionEntity> saveEntitys = new ArrayList<>();

        for (PermissionEntity entity:listEntity) {
            entity.setStatus(CommonStaticWord.Ban_Status_1);
        }

        for (InitPermission vo : listVO) {
            int flag = 0;

            for (PermissionEntity entity:listEntity) {
                if(StringUtils.isNotBlank(vo.getCode())
                        && vo.getCode().equals(entity.getCode())){
                    flag = 1;
                    entity.setStatus(CommonStaticWord.Normal_Status_0);
                }
            }

            if(flag == 0){
                PermissionEntity permissionEntity = new PermissionEntity();
                BeanUtils.copyProperties(vo,permissionEntity);
                //permissionEntity.setId(null);
                permissionEntity.setCreateUser(1);
                permissionEntity.setCreateTime(new Timestamp(new Date().getTime()));
                permissionEntity.setUpdateUser(1);
                permissionEntity.setUpdateTime(new Timestamp(new Date().getTime()));
                permissionEntity.setStatus(1);
                saveEntitys.add(permissionEntity);

            }
        }
        this.dbService.save(saveEntitys);


        return 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public List<UserPermission> listUserPermission(List<Integer> ids){
        return this.permissionMapper.listUserPermission(ids);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public List<PermissionEntity> listPermissionEntity(Map<String,Object> paramMap){
        return this.permissionMapper.listPermissionEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public Integer listPermissionEntityCount(Map<String,Object> paramMap){
        return this.permissionMapper.listPermissionEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PermissionEntity getPermission(PermissionEntity param) throws Exception {
        return this.dbService.get(param);
    }
}



















