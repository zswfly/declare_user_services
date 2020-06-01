package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.CompanyDto;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.ResponseCode;
import com.zsw.utils.UserStaticURLUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/29.
 */
@RestController
@RequestMapping(UserStaticURLUtil.companyController)
public class CompanyController extends BaseController {

    @Autowired
    ICompanyService companyService;

    @Autowired
    RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(CompanyController.class);




    @RequestMapping(value=UserStaticURLUtil.companyController_newCompany,
            method= RequestMethod.POST)
    //    @AdminPermission(code = "user.companyController.newCompany",name = "新增公司",description ="新增公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_newCompany)
    public String newCompany(CompanyDto companyDto,@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.companyService.checkCompanyExist(companyDto);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.companyService.newCompany(companyDto,currentAdminUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

//    @RequestMapping(value=UserStaticURLUtil.companyController_deleteCompany,
//            method= RequestMethod.DELETE)
//    //    @AdminPermission(code = "user.companyController.deleteCompany",name = "删除公司",description ="删除(禁用)公司"
////            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_deleteCompany)
//    public String deleteCompany(Integer companyId,@RequestHeader("userId") Integer currentUserId) throws Exception {
//        try {
//            ResponseJson responseJson = new ResponseJson();
//            Gson gson = new Gson();
//
//            this.companyService.deleteCompany(companyId,currentUserId);
//
//            responseJson.setCode(ResponseCode.Code_200);
//            responseJson.setMessage("删除成功");
//
//            return gson.toJson(responseJson);
//        }catch (Exception e){
//            e.printStackTrace();
//            return CommonUtils.ErrorResposeJson();
//        }
//    }


    @RequestMapping(value=UserStaticURLUtil.companyController_updateCompany,
            method= RequestMethod.PUT)
    //    @AdminPermission(code = "user.companyController.updateCompany",name = "更新公司",description ="更新公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_updateCompany)
    public String updateCompany(CompanyDto companyDto,@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.companyService.checkCompanyExist(companyDto);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.companyService.updateCompany(companyDto,currentAdminUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=UserStaticURLUtil.companyController_getCompany+"/{companyId}",
            method= RequestMethod.GET)
//    @AdminPermission(code = "user.companyController.getCompany",name = "获取公司",description ="根据id获取公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_getCompany)
    public String getCompany(@PathVariable Integer companyId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(companyId);
            companyEntity = this.companyService.getCompany(companyEntity);
            companyEntity.setUrl(null);
            if(companyEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没公司");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(companyEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=UserStaticURLUtil.companyController_companysPage,
            method= RequestMethod.GET)
//    @AdminPermission(code = "user.companyController.companysPage",name = "搜索公司",description ="搜索公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_companysPage)
    public String companysPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String companyName = request.getParameter("companyName");
            if(companyName !=null && StringUtils.isNotEmpty(companyName)) {
                paramMap.put("companyName", companyName);
            }

            String mnemonicCode = request.getParameter("mnemonicCode");
            if(mnemonicCode !=null && StringUtils.isNotEmpty(mnemonicCode)) {
                paramMap.put("mnemonicCode", mnemonicCode);
            }

            String beginCreateTime = request.getParameter("beginCreateTime");
            if(beginCreateTime !=null && StringUtils.isNotEmpty(beginCreateTime)) {
                paramMap.put("beginCreateTime", beginCreateTime);
            }
            String endCreateTime = request.getParameter("endCreateTime");
            if(endCreateTime !=null && StringUtils.isNotEmpty(endCreateTime)) {
                paramMap.put("endCreateTime", endCreateTime);
            }

            String size = request.getParameter("size");
            if(size !=null && StringUtils.isNotEmpty(size)) {
                paramMap.put("companySize", Integer.valueOf(NumberUtils.toInt(size, 0)));
            }

            String creatorId = request.getParameter("creatorId");
            if(creatorId !=null && StringUtils.isNotEmpty(creatorId)) {
                paramMap.put("creatorId", Integer.valueOf(NumberUtils.toInt(creatorId, 0)));
            }

            Integer currentPage = Integer.valueOf(NumberUtils.toInt(request.getParameter("currentPage"), 1));
            Integer pageSize = Integer.valueOf(NumberUtils.toInt(request.getParameter("pageSize"), 10));

            paramMap.put("start", (currentPage-1)*pageSize);
            paramMap.put("pageSize", pageSize);

            Map<String,Object> data = new HashMap<>();
            List<CompanyEntity> items = this.companyService.listCompanyEntity(paramMap);
            for(CompanyEntity temp : items){
                temp.setUrl(null);
            }
            Integer total = this.companyService.listCompanyEntityCount(paramMap);
            data.put("items",items);
            data.put("total",total==null?0:total);
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }










    @Override
    public Logger getLOG(){
        return this.LOG;
    }


    }
