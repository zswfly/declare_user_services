package com.zsw.services;

import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.user.CompanyDto;
import com.zsw.entitys.user.SimpleCompanyDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
public interface ICompanyService extends IBaseService{
    List<SimpleCompanyDto> listSimpleCompanyDto( Map<String,Object> paramMap)throws Exception;
    Integer listCompanyEntityCount( Map<String,Object> paramMap)throws Exception;
    List<CompanyEntity> listCompanyEntity(Map<String,Object> paramMap)throws Exception;
    SimpleCompanyDto getSimpleCompanyDto( Map<String,Object> paramMap)throws Exception;
    void checkCompanyContract(CompanyEntity companyEntity)throws Exception;

    void newCompany(CompanyDto companyDto, Integer currentUserId)throws Exception;
    CompanyEntity deleteCompany(Integer companyId, Integer currentUserId)throws Exception;
    CompanyEntity updateCompany(CompanyDto companyDto , Integer currentUserId)throws Exception;
    CompanyEntity getCompany(CompanyEntity param)throws Exception;


    String checkCompanyExist(CompanyDto companyDto) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;
}
