package com.zsw.daos;

import com.zsw.entitys.CompanyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CompanyMapper {
    List<CompanyEntity> listCompanyEntity(@Param("paramMap") Map<String,Object> paramMap);
    Integer listCompanyEntityCount(@Param("paramMap") Map<String,Object> paramMap);
    CompanyEntity getCompanyEntity(@Param("paramMap") Map<String,Object> paramMap);
    List<Integer> checkCompanyManagerIds(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);
}