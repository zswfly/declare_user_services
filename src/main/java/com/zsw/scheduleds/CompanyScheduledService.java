package com.zsw.scheduleds;

import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IDBService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/30.
 */
@Component
public class CompanyScheduledService {
    @Autowired
    private IDBService dbService;

    @Autowired
    private ICompanyService companyService;

    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void checkCompanyContract() {
        try {
            System.out.println("CompanyScheduledService.checkCompanyContract 1111111111111111________Scheduled Task-------------------------------------------------------------------");
            Integer start = 0;
            Integer pageSize = 1;
            //Integer pageSize = 30;
            while(true){
                Map<String, Object> listCompanyEntityParam = new HashMap<>();
                listCompanyEntityParam.put("start",start);
                listCompanyEntityParam.put("pageSize",pageSize);
                List<CompanyEntity> listCompanyEntity = this.companyService.listCompanyEntity(listCompanyEntityParam);
                for(CompanyEntity companyEntity : listCompanyEntity){
                    this.companyService.checkCompanyContract(companyEntity);
                    SimpleCompanyDto simpleCompanyDto = new SimpleCompanyDto();
                    BeanUtils.copyProperties(companyEntity,simpleCompanyDto);
                }
                if(listCompanyEntity == null || listCompanyEntity.size() == 0) break;
                start += 30;
            }
            System.out.println("2222222222222222________Scheduled Task-------------------------------------------------------------------");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
