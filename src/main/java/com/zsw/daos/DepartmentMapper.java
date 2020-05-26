package com.zsw.daos;


import com.zsw.entitys.DepartmentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DepartmentMapper {

    List<DepartmentEntity> listDepartmentEntity(@Param("paramMap") Map<String,Object> paramMap);
    Integer listDepartmentEntityCount(@Param("paramMap") Map<String,Object> paramMap);


    Map<String,String> checkDepartmentExist(@Param("paramMap") Map<String,Object> paramMap);

}