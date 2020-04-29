package com.zsw.daos;

import com.zsw.entitys.user.SimpleCompanyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CompanyMapper {
    List<SimpleCompanyDto> listSimpleCompanyDto(@Param("paramMap") Map<String,Object> paramMap);
    SimpleCompanyDto getSimpleCompanyDto(@Param("paramMap") Map<String,Object> paramMap);
}