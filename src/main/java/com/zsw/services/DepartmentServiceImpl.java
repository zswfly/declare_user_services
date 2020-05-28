package com.zsw.services;

import com.zsw.daos.DepartmentMapper;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.DepartmentUserEntity;
import com.zsw.entitys.user.DepartmentDto;
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
 * Created by zhangshaowei on 2020/5/21.
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService,Serializable {

    private static final long serialVersionUID = -7251344524243762049L;

    @Autowired
    private IDBService dbService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer  currentCompanyId) throws Exception {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        BeanUtils.copyProperties(departmentDto,departmentEntity);
        departmentEntity.setId(null);
        departmentEntity.setStatus(CommonStaticWord.Normal_Status_0);
        if(currentCompanyId != null && currentCompanyId > 0)
            departmentEntity.setCompanyId(currentCompanyId);
        departmentEntity.setMnemonicCode(PinyinUtils.getFirstSpell(departmentEntity.getName()));
        departmentEntity.setCreateUser(currentUserId);
        departmentEntity.setCreateTime(new Timestamp(new Date().getTime()));
        departmentEntity.setUpdateUser(currentUserId);
        departmentEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(departmentEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void updateStatusDepartment(List<Integer> ids, String type, Integer currentUserId,Integer currentCompanyId) throws Exception {

        List<DepartmentEntity> list =  this.dbService.findBy(DepartmentEntity.class,"id",ids);
        Integer status = null;
        if( CommonStaticWord.Status_ban.equals(type)){
            status = CommonStaticWord.Ban_Status_1;
        }else if(CommonStaticWord.Status_enable.equals(type)){
            status = CommonStaticWord.Normal_Status_0;
        }

        for(DepartmentEntity item : list){
            if(currentCompanyId == null
                    || currentCompanyId < 1
                    || (currentCompanyId > 0 && currentCompanyId == item.getCompanyId())
                    ){
                item.setStatus(status);
                item.setUpdateUser(currentUserId);
                item.setUpdateTime(new Timestamp(new Date().getTime()));
            }

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public DepartmentEntity updateDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer  currentCompanyId) throws Exception {
        if(departmentDto == null
                || departmentDto.getId() == null){
            throw new Exception("参数错误");
        }
        DepartmentEntity param = new DepartmentEntity();
        param.setId(departmentDto.getId());
        if(currentCompanyId != null && currentCompanyId > 0)
            param.setCompanyId(currentCompanyId);
        DepartmentEntity departmentEntity = this.dbService.get(param);

        if(departmentEntity == null) throw new Exception("没有该公司id");

        BeanUtils.copyProperties(departmentDto,departmentEntity);
        if(currentCompanyId != null && currentCompanyId > 0)
            departmentEntity.setCompanyId(currentCompanyId);
        departmentEntity.setUpdateUser(currentUserId);
        departmentEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return departmentEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public DepartmentEntity getDepartment(DepartmentEntity param) throws Exception {
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listDepartmentEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.departmentMapper.listDepartmentEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DepartmentEntity> listDepartmentEntity(Map<String, Object> paramMap) throws Exception {
        return this.departmentMapper.listDepartmentEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void relationDepartmentUser(List<DepartmentUserEntity> listDepartmentUser, Integer currentUserId) throws Exception {
        for(DepartmentUserEntity departmentUserEntity:listDepartmentUser ){
            departmentUserEntity.setCreateUser(currentUserId);
            departmentUserEntity.setCreateTime(new Timestamp(new Date().getTime()));
            departmentUserEntity.setUpdateUser(currentUserId);
            departmentUserEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        }
        this.dbService.save(listDepartmentUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void deleteDepartmentUser(List<DepartmentUserEntity> listDepartmentUser, Integer currentUserId) throws Exception {
        this.dbService.delete(listDepartmentUser);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String checkDepartmentExist(DepartmentDto departmentDto, Integer currentCompanyId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("companyId",currentCompanyId);
        paramMap.put("departmentName",departmentDto.getName());
        if(departmentDto.getId() != null && departmentDto.getId() >0 ){
            paramMap.put("notDepartmentId",departmentDto.getId());
        }
        Map<String,String> result = this.departmentMapper.checkDepartmentExist(paramMap);
        String departmentName = result.get("department_name");
        StringBuilder stringBuilder = new StringBuilder();
        if(result != null &&
                (
                        (StringUtils.isNotBlank(departmentName)&&StringUtils.isNotEmpty(departmentName))
                )
        ){
            List<String> departmentNames = Arrays.asList(departmentName.split(","));
            if(departmentNames.contains(departmentDto.getName())){
                stringBuilder.append("当前部门名已存在;");
            }
        }

        return stringBuilder.toString();
    }
}
