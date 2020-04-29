package com.zsw.controllers;

import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.services.ICompanyService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.ResponseCode;
import com.zsw.utils.UserStaticURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
@Controller
@RequestMapping(UserStaticURLUtil.companyController)
public class CompanyController {

    @Autowired
    ICompanyService companyService;

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping(value=UserStaticURLUtil.companyController_selectUserCompany,
            method= RequestMethod.GET)
    @ResponseBody
    public Result<HashMap<String, Object>> selectUserCompany(Integer companyId, @RequestHeader("userId") Integer currentUserId) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();
        try {
            if (companyId == null) {
                result.setCode(ResponseCode.Code_0);
                result.setMessage("选择公司为空");
                return result;
            }
            if (currentUserId == null) {
                result.setCode(ResponseCode.Code_0);
                result.setMessage("用户没有登录");
                return result;
            }
            Map<String, Object> listSimpleCompanyDtoParam = new HashMap<>();
            List<SimpleCompanyDto> listSimpleCompanyDto = this.companyService.listSimpleCompanyDto(listSimpleCompanyDtoParam);
            if (listSimpleCompanyDto == null  ) {
                result.setCode(ResponseCode.Code_0);
                result.setMessage("当前用户没有该公司权限");
                return result;
            } else if(listSimpleCompanyDto.size() != 1){
                result.setCode(ResponseCode.Code_0);
                result.setMessage("数据错误,请联系工作人员");
                return result;
            }else if(listSimpleCompanyDto.get(0).getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_0);
                result.setMessage("该公司已禁用或已过使用期限");
                return result;
            }else{
                HashMap<String,Object> data = new HashMap<>();
                data.put("userId",currentUserId);
                data.put("hostUrl",listSimpleCompanyDto.get(0).getUrl() );
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResponseCode.Code_0);
            result.setMessage("系统错误");
            return result;
        }
    }
}
