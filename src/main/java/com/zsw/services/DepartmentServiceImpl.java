package com.zsw.services;

import com.zsw.daos.DepartmentMapper;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.user.DepartmentDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.PinyinUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/21.
 */
public class DepartmentServiceImpl implements IDepartmentService,Serializable {

    private static final long serialVersionUID = -7251344524243762049L;

    @Autowired
    private IDBService dbService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public void newDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer  currentCompanyId) throws Exception {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        BeanUtils.copyProperties(departmentDto,departmentEntity);
        departmentEntity.setId(null);
        departmentEntity.setCompanyId(currentCompanyId);
        departmentEntity.setMnemonicCode(PinyinUtils.getFirstSpell(departmentEntity.getName()));
        departmentEntity.setCreateUser(currentUserId);
        departmentEntity.setCreateTime(new Timestamp(new Date().getTime()));
        departmentEntity.setUpdateUser(currentUserId);
        departmentEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(departmentEntity);
    }

    @Override
    public DepartmentEntity deleteDepartment(Integer departmentId, Integer currentUserId,Integer currentCompanyId) throws Exception {
        DepartmentEntity param = new DepartmentEntity();
        param.setId(departmentId);
        param.setCompanyId(currentCompanyId);
        DepartmentEntity departmentEntity = this.dbService.get(param);
        departmentEntity.setStatus(CommonStaticWord.Ban_Status_1);
        return departmentEntity;
    }

    @Override
    public DepartmentEntity updateDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer  currentCompanyId) throws Exception {
        if(departmentDto == null
                || departmentDto.getId() == null){
            throw new Exception("参数错误");
        }
        DepartmentEntity param = new DepartmentEntity();
        param.setId(departmentDto.getId());
        param.setCompanyId(currentCompanyId);
        DepartmentEntity departmentEntity = this.dbService.get(param);

        if(departmentEntity == null) throw new Exception("没有该公司id");

        BeanUtils.copyProperties(departmentDto,departmentEntity);

        departmentEntity.setCompanyId(currentCompanyId);
        departmentEntity.setMnemonicCode(PinyinUtils.getFirstSpell(departmentEntity.getName()));
        departmentEntity.setUpdateUser(currentUserId);
        departmentEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return departmentEntity;
    }

    @Override
    public DepartmentEntity getDepartment(DepartmentEntity param) throws Exception {
        return this.dbService.get(param);
    }

    @Override
    public Integer listDepartmentEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.departmentMapper.listDepartmentEntityCount(paramMap);
    }

    @Override
    public List<DepartmentEntity> listDepartmentEntity(Map<String, Object> paramMap) throws Exception {
        return this.departmentMapper.listDepartmentEntity(paramMap);
    }
}
