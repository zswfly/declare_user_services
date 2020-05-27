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


    @RequestMapping(value=UserStaticURLUtil.companyController_selectUserCompany,
            method= RequestMethod.GET)
    public Result<HashMap<String, Object>> selectUserCompany(Integer companyId, @RequestHeader("userId") Integer currentUserId,@RequestHeader("rememberToken") String rememberToken) throws Exception {
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
            if (StringUtils.isBlank(rememberToken) || StringUtils.isEmpty(rememberToken)) {
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("rememberToken有问题");
                return result;
            }
            Map<String, Object> listSimpleCompanyDtoParam = new HashMap<>();
            listSimpleCompanyDtoParam.put("companyId",companyId);
            listSimpleCompanyDtoParam.put("userId",currentUserId);
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
                data.put("companyId",companyId);
                data.put("rememberToken",rememberToken);
                data.put("hostUrl",listSimpleCompanyDto.get(0).getUrl() );
                result.setData(data);
                result.setCode(ResponseCode.Code_200);
                return result;
            }
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            result.setCode(ResponseCode.Code_500);
            result.setMessage("系统错误");
            return result;
        }
    }

    @RequestMapping(value=UserStaticURLUtil.companyController_newCompany,
            method= RequestMethod.POST)
    //    @Permission(code = "user.companyController.newCompany",name = "新增公司",description ="新增公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_newCompany)
    public String newCompany(CompanyDto companyDto,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.companyService.newCompany(companyDto,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }

//    @RequestMapping(value=UserStaticURLUtil.companyController_deleteCompany,
//            method= RequestMethod.DELETE)
//    //    @Permission(code = "user.companyController.deleteCompany",name = "删除公司",description ="删除(禁用)公司"
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
    //    @Permission(code = "user.companyController.updateCompany",name = "更新公司",description ="更新公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_updateCompany)
    public String updateCompany(CompanyDto companyDto,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.companyService.updateCompany(companyDto,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.companyController_getCompany+"/{companyId}",
            method= RequestMethod.GET)
//    @Permission(code = "user.companyController.getCompany",name = "获取公司",description ="根据id获取公司"
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
            return CommonUtils.ErrorResposeJson();
        }
    }




    @RequestMapping(value=UserStaticURLUtil.companyController_companysPage,
            method= RequestMethod.GET)
//    @Permission(code = "user.companyController.companysPage",name = "搜索公司",description ="搜索公司"
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
            return CommonUtils.ErrorResposeJson();
        }
    }



    @RequestMapping(value=UserStaticURLUtil.companyController_getUserCompanys,
            method= RequestMethod.GET)
//    @Permission(code = "user.companyController.getUserCompanys",name = "获取当前用户的公司",description ="获取当前用户所属的所有公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.companyController + UserStaticURLUtil.companyController_getUserCompanys)
    public String getUserCompanys(@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> listSimpleCompanyDtoParams = new HashMap<>();
            listSimpleCompanyDtoParams.put("userId",currentUserId+"");
            List<SimpleCompanyDto> simpleCompanyDtoList = this.companyService.listSimpleCompanyDto(listSimpleCompanyDtoParams);
            if(simpleCompanyDtoList == null || simpleCompanyDtoList.size() < 1){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该用户没有公司数据");
            }else{
                for(SimpleCompanyDto dto : simpleCompanyDtoList){
                    dto.setUrl(null);
                }
                HashMap<String,Object> data = new HashMap<>();
                data.put("items",simpleCompanyDtoList);
                responseJson.setData(data);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("成功");
            }
            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }






    @Override
    public Logger getLOG(){
        return this.LOG;
    }


    }
