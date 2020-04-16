package com.zsw.services;

import com.zsw.daos.PermissionMapper;
import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;
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
import java.util.Date;
import java.util.List;

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

        for (PermissionEntity entity:listEntity) {
            entity.setStatus(0);
        }

        for (InitPermission vo : listVO) {
            int flag = 0;

            for (PermissionEntity entity:listEntity) {
                if(StringUtils.isNotBlank(vo.getCode())
                        && vo.getCode().equals(entity.getCode())){

                    flag = 1;
                    entity.setStatus(1);

                }
            }

            if(flag == 0){
                PermissionEntity permissionEntity = new PermissionEntity();
                BeanUtils.copyProperties(vo,permissionEntity);
                permissionEntity.setCreateUser(1);
                permissionEntity.setCreateTime(new Timestamp(new Date().getTime()));
                permissionEntity.setUpdateUser(1);
                permissionEntity.setUpdateTime(new Timestamp(new Date().getTime()));
                permissionEntity.setStatus(1);
                this.dbService.save(permissionEntity);
            }
        }



        return 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public List<UserPermission> listUserPermission(List<Integer> ids){
        return this.permissionMapper.listUserPermission(ids);
    }
}



















