package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.CompanyDto;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.PinyinUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
@Service
public class CompanyImpl implements ICompanyService,Serializable{


    private static final long serialVersionUID = 5730946426593664068L;

    @Autowired
    private IDBService dbService;

    @Resource
    private CompanyMapper companyMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listCompanyEntityCount(Map<String, Object> paramMap) {
        return this.companyMapper.listCompanyEntityCount(paramMap);
    }

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
        if(companyEntity.getStatus() == CommonStaticWord.Normal_Status_0
                &&
                    (contractStartAt == null
                    || contractEndAt == null
                    || contractStartAt.after(currentDate)
                    || contractEndAt.before(currentDate))
                ){
            companyEntity.setStatus(CommonStaticWord.Ban_Status_1);
            this.dbService.update(companyEntity);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CompanyEntity> listCompanyEntity(Map<String, Object> paramMap) {
        return this.companyMapper.listCompanyEntity(paramMap);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newCompany(CompanyDto companyDto, Integer currentUserId) throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
        BeanUtils.copyProperties(companyDto,companyEntity);
        companyEntity.setMnemonicCode(PinyinUtils.getFirstSpell(companyEntity.getName()));
        companyEntity.setId(null);
        companyEntity.setStatus(CommonStaticWord.Normal_Status_0);
        companyEntity.setCreatorId(currentUserId);
        companyEntity.setCreateUser(currentUserId);
        companyEntity.setCreateTime(new Timestamp(new Date().getTime()));
        companyEntity.setUpdateUser(currentUserId);
        companyEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(companyEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public CompanyEntity deleteCompany(Integer companyId, Integer currentUserId) throws Exception {
        CompanyEntity companyEntity = this.dbService.get(CompanyEntity.class,companyId);
        companyEntity.setStatus(CommonStaticWord.Ban_Status_1);
        companyEntity.setUpdateUser(currentUserId);
        companyEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return companyEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public CompanyEntity updateCompany(CompanyDto companyDto , Integer currentUserId) throws Exception {

        if(companyDto == null
                || companyDto.getId() == null){
            throw new Exception("参数错误");
        }

        CompanyEntity companyEntity = this.dbService.get(CompanyEntity.class,companyDto.getId());

        if(companyEntity == null) throw new Exception("没有该公司id");

        if(companyDto.getUrl() == null )companyDto.setUrl(companyEntity.getUrl());
        BeanUtils.copyProperties(companyDto,companyEntity);

        //companyEntity.setMnemonicCode(PinyinUtils.getFirstSpell(companyEntity.getName()));
        companyEntity.setUpdateUser(currentUserId);
        companyEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return companyEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CompanyEntity getCompany(CompanyEntity param) throws Exception {
        return this.dbService.get(param);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkCompanyExist(CompanyDto companyDto) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(companyDto.getId() == null){

            CompanyEntity param = new CompanyEntity();

            param.setName(companyDto.getName());
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("公司名已存在");

        }else{
            CompanyEntity param = new CompanyEntity();
            List<CompanyEntity> resultList = null;
            param.setName(companyDto.getName());
            resultList = this.dbService.find(param);
            for(CompanyEntity result :resultList){
                result = this.dbService.get(param);
                if( result != null && result.getId() != companyDto.getId() ) {
                    stringBuilder.append("公司名已存在");
                    break;
                }
            }

        }

        return stringBuilder.toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception{
        int status = CommonStaticWord.Status_ban.equals(type)?CommonStaticWord.Ban_Status_1:CommonStaticWord.Normal_Status_0 ;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId",currentUserId);
        paramMap.put("status",status);
        paramMap.put("ids",ids);
        this.companyMapper.batchBan(paramMap);

    }


}
