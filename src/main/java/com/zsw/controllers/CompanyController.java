package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.ResponseCode;
import com.zsw.utils.UserStaticURLUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

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
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("选择公司为空");
                return result;
            }
            if (currentUserId == null) {
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("用户没有登录");
                return result;
            }
            Map<String, Object> listSimpleCompanyDtoParam = new HashMap<>();
            listSimpleCompanyDtoParam.put("companyId",companyId);
            List<SimpleCompanyDto> listSimpleCompanyDto = this.companyService.listSimpleCompanyDto(listSimpleCompanyDtoParam);
            if (listSimpleCompanyDto == null  ) {
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("当前用户没有该公司权限");
                return result;
            } else if(listSimpleCompanyDto.size() != 1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("数据错误,请联系工作人员");
                return result;
            }else if(listSimpleCompanyDto.get(0).getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("该公司已禁用或已过使用期限");
                return result;
            }else{
                HashMap<String,Object> data = new HashMap<>();
                data.put("userId",currentUserId);
                data.put("hostUrl",listSimpleCompanyDto.get(0).getUrl() );
                result.setData(data);
                result.setCode(ResponseCode.Code_200);
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResponseCode.Code_500);
            result.setMessage("系统错误");
            return result;
        }
    }

    @RequestMapping(value=UserStaticURLUtil.companyController_companysPage,
            method= RequestMethod.GET)
    @ResponseBody
    public String companysPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotBlank(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String companyName = request.getParameter("companyName");
            if(companyName !=null && StringUtils.isNotBlank(companyName)) {
                paramMap.put("companyName", companyName);
            }

            String beginCreateTime = request.getParameter("beginCreateTime");
            if(beginCreateTime !=null && StringUtils.isNotBlank(beginCreateTime)) {
                paramMap.put("beginCreateTime", beginCreateTime);
            }
            String endCreateTime = request.getParameter("endCreateTime");
            if(endCreateTime !=null && StringUtils.isNotBlank(endCreateTime)) {
                paramMap.put("endCreateTime", endCreateTime);
            }

            String size = request.getParameter("size");
            if(size !=null && StringUtils.isNotBlank(size)) {
                paramMap.put("size", Integer.valueOf(NumberUtils.toInt(size, 0)));
            }

            String creatorId = request.getParameter("creatorId");
            if(creatorId !=null && StringUtils.isNotBlank(creatorId)) {
                paramMap.put("creatorId", Integer.valueOf(NumberUtils.toInt(creatorId, 0)));
            }

            Integer currentPage = Integer.valueOf(NumberUtils.toInt(request.getParameter("currentPage"), 1));
            Integer pageSize = Integer.valueOf(NumberUtils.toInt(request.getParameter("pageSize"), 10));

            paramMap.put("start", (currentPage-1)*pageSize);
            paramMap.put("pageSize", pageSize);

            Map<String,Object> data = new HashMap<>();
            List<CompanyEntity> items = this.companyService.listCompanyEntity(paramMap);
            data.put("items",items);
            data.put("total",items==null?0:items.size());
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_String_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.companyController_newCompany,
            method= RequestMethod.POST)
    @ResponseBody
    public String newUser(UserDto userDto) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }












    }
