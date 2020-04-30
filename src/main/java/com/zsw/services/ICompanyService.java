package com.zsw.services;

import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.user.SimpleCompanyDto;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
public interface ICompanyService extends IBaseService{
    List<SimpleCompanyDto> listSimpleCompanyDto( Map<String,Object> paramMap);
    List<CompanyEntity> listCompanyEntity(Map<String,Object> paramMap);
    SimpleCompanyDto getSimpleCompanyDto( Map<String,Object> paramMap);
    void checkCompanyContract(CompanyEntity companyEntity);
}
