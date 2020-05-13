package com.zsw.daos;

import com.zsw.entitys.CompanyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompanyMapper {
    List<CompanyEntity> listCompanyEntity(@Param("paramMap") Map<String,Object> paramMap);
    CompanyEntity getCompanyEntity(@Param("paramMap") Map<String,Object> paramMap);
}