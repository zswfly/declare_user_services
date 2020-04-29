package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.user.SimpleCompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
@Service
public class CompanyImpl implements ICompanyService{


    private static final long serialVersionUID = 5730946426593664068L;

    @Autowired
    private IDBService dbService;

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public List<SimpleCompanyDto> listSimpleCompanyDto(Map<String, Object> paramMap) {
        return this.companyMapper.listSimpleCompanyDto(paramMap);
    }

    @Override
    public SimpleCompanyDto getSimpleCompanyDto(Map<String, Object> paramMap) {
        return this.companyMapper.getSimpleCompanyDto(paramMap);
    }


}
