package com.zsw.services;

import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.DepartmentUserEntity;
import com.zsw.entitys.user.DepartmentDto;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/21.
 */
public interface IDepartmentService extends IBaseService{


    void newDepartment(DepartmentDto departmentDto, Integer currentUserId,Integer currentCompanyId)throws Exception;;
    void updateStatusDepartment(List<Integer> ids, String type, Integer currentUserId,Integer currentCompanyId)throws Exception;;
    DepartmentEntity updateDepartment(DepartmentDto departmentDto , Integer currentUserId,Integer currentCompanyId)throws Exception;;
    DepartmentEntity getDepartment(DepartmentEntity param)throws Exception;

    Integer listDepartmentEntityCount( Map<String,Object> paramMap)throws Exception;
    List<DepartmentEntity> listDepartmentEntity(Map<String,Object> paramMap)throws Exception;

    void relationDepartmentUser(List<DepartmentUserEntity> listDepartmentUser,Integer currentUserId)throws Exception;
    void deleteDepartmentUser(List<DepartmentUserEntity> listDepartmentUser,Integer currentUserId)throws Exception;
    String checkDepartmentExist(DepartmentDto departmentDto,Integer currentCompanyId)throws Exception;

    void saveDepartmentUserEntity(DepartmentUserEntity departmentUserEntity)throws Exception;
}
