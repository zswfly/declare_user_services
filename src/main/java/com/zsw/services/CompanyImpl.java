package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.utils.CommonStaticWord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public List<SimpleCompanyDto> listSimpleCompanyDto(Map<String, Object> paramMap) {
        List<CompanyEntity> listCompanyEntity = this.companyMapper.listCompanyEntity(paramMap);
        List<SimpleCompanyDto> listSimpleCompanyDto = new ArrayList<>();
        for(CompanyEntity companyEntity : listCompanyEntity){
            this.checkCompanyContract(companyEntity);
            SimpleCompanyDto simpleCompanyDto = new SimpleCompanyDto();
            BeanUtils.copyProperties(companyEntity,simpleCompanyDto);
            listSimpleCompanyDto.add(simpleCompanyDto);
        }
        return listSimpleCompanyDto ;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public SimpleCompanyDto getSimpleCompanyDto(Map<String, Object> paramMap) {
        CompanyEntity companyEntity = this.companyMapper.getCompanyEntity(paramMap);
        this.checkCompanyContract(companyEntity);
        SimpleCompanyDto simpleCompanyDto = new SimpleCompanyDto();
        BeanUtils.copyProperties(companyEntity,simpleCompanyDto);
        return simpleCompanyDto ;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkCompanyContract(CompanyEntity companyEntity){
        Date currentDate = new Date();
        Date contractStartAt = companyEntity.getContractStartAt();
        Date contractEndAt = companyEntity.getContractEndAt();
        if(contractStartAt == null
                || contractEndAt == null
                || contractStartAt.after(currentDate)
                || contractEndAt.before(currentDate)
                ){
            companyEntity.setStatus(CommonStaticWord.Ban_Status_1);
            this.dbService.update(companyEntity);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CompanyEntity> listCompanyEntity(Map<String, Object> paramMap) {
        List<CompanyEntity> listCompanyEntity = this.companyMapper.listCompanyEntity(paramMap);
        return listCompanyEntity ;
    }
}
