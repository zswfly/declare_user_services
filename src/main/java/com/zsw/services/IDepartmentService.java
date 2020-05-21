package com.zsw.services;

import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.user.DepartmentDto;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/21.
 */
public interface IDepartmentService extends IBaseService{


    void newDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer currentCompanyId)throws Exception;;
    DepartmentEntity deleteDepartment(Integer departmentId, Integer currentUserId,Integer currentCompanyId)throws Exception;;
    DepartmentEntity updateDepartment(DepartmentDto departmentDto , Integer currentUserId,Integer currentCompanyId)throws Exception;;
    DepartmentEntity getDepartment(DepartmentEntity param)throws Exception;

    Integer listDepartmentEntityCount( Map<String,Object> paramMap)throws Exception;
    List<DepartmentEntity> listDepartmentEntity(Map<String,Object> paramMap)throws Exception;


}
