package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.SimpleCompanyDto;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IUserService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
@RestController
@RequestMapping(UserStaticURLUtil.userController)
public class UserController extends BaseController{

    @Autowired
    IUserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ICompanyService companyService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value=UserStaticURLUtil.userController_login,
            method= RequestMethod.POST)
    public Result<HashMap<String, Object>> login(LoginTemp loginTemp) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();
        try{
            UserEntity userEntity = null;
            UserEntity paramUserEntity = new UserEntity();
            paramUserEntity.setPhone(loginTemp.getPhone());
            userEntity = this.userService.getUser(paramUserEntity);

            if(userEntity.getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("账户禁用");
            }

            return UserUtils.login(this.userService,this.restTemplate,loginTemp,userEntity,Boolean.FALSE);
        }catch (Exception e){

            CommonUtils.ErrorAction(LOG,e);
            result.setCode(ResponseCode.Code_500);
            result.setMessage("系统错误");
            return result;
        }

    }

    @RequestMapping(value=UserStaticURLUtil.userController_selectUserCompany,
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

    @RequestMapping(value=UserStaticURLUtil.userController_getUserCompanys,
            method= RequestMethod.GET)
//    @Permission(code = "user.userController.getUserCompanys",name = "获取当前用户的公司",description ="获取当前用户所属的所有公司"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_getUserCompanys)
    public String getUserCompanys(@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
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
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_loginOut,
            method= RequestMethod.POST)
//    @Permission(code = "user.userController.loginOut",name = "登出用户",description ="登出用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_loginOut)
    public String loginOut(@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            return UserUtils.loginOut(this.userService,this.restTemplate,currentUserId);
        } catch (Exception e) {
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }
    @RequestMapping(value=UserStaticURLUtil.userController_checkRememberToken,
            method= RequestMethod.POST)
    public Boolean checkRememberToken( @RequestBody Map<String,String> args) throws Exception {
        try {
            return UserUtils.checkRememberToken(this.userService,this.restTemplate,args);
        } catch (Exception e) {
            CommonUtils.ErrorAction(LOG,e);
            return Boolean.FALSE;
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_resetPassWord,
            method= RequestMethod.POST)
    public String resetPassWord(LoginTemp loginTemp) throws Exception {
        try {
            return UserUtils.resetPassWord(this.userService,this.restTemplate,loginTemp);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }

    }

    @RequestMapping(value=UserStaticURLUtil.userController_newUser,
            method= RequestMethod.POST)
//    @Permission(code = "user.userController.newUser",name = "新增用户",description ="新增用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_newUser)
    public String newUser(UserDto userDto,@RequestHeader("userId") Integer currentUserId,Integer departmentId) throws Exception {
        try {
            return OperationUserUtils.newUser(this.userService,null,userDto,currentUserId,departmentId,false);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_getUser+"/{userId}",
            method= RequestMethod.GET)
    @Permission(code = "user.userController.getUser",name = "获取用户",description ="根据id获取用户"
            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_getUser)
    public String getUser(@PathVariable Integer userId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationUserUtils.getUser(this.userService,userId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }



    @RequestMapping(value=UserStaticURLUtil.userController_updateUser,
            method= RequestMethod.PUT)
//    @Permission(code = "user.userController.updateUser",name = "更新用户",description ="更新用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_updateUser)
    public String updateUser(UserDto userDto,@RequestHeader("userId") Integer currentUserId/*,@RequestHeader("companyId") Integer currentCompanyId*/) throws Exception {
        try {
            return OperationUserUtils.updateUser(this.userService,userDto,currentUserId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "user.userController.batchBan",name = "批量禁用/恢复",description ="批量禁用/恢复用户"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_batchBan)
    public String batchBan( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationUserUtils.batchBan(this.userService,params,currentUserId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_usersPage,
            method= RequestMethod.GET)
    //@Permission(code = "user.userController.usersPage",name = "条件搜索用户",description ="条件搜索用户"
    //        ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_usersPage)
    public String usersPage(NativeWebRequest request,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationUserUtils.usersPage(this.userService,request,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


    @RequestMapping(value=UserStaticURLUtil.userController_relationUserRole,
            method= RequestMethod.PUT)
    //@Permission(code = "user.roleController.relationRolePermission",name = "关联角色权限",description ="关联角色权限"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleController + UserStaticURLUtil.roleController_relationRolePermission)
    public String relationUserRole( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            String roleIds = params.get("roleIds");
            Integer userId = Integer.valueOf(NumberUtils.toInt(params.get("userId"), 0));
            if(roleIds == null || userId < 1){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(roleIds, Integer[].class));
                this.userService.relationOrDeleteUserRole(list,userId,currentUserId,currentCompanyId,Boolean.FALSE);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
                return gson.toJson(responseJson);
            }

        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=UserStaticURLUtil.userController_deleteUserRole,
            method= RequestMethod.PUT)
    //@Permission(code = "user.roleController.deleteRolePermission",name = "删除角色权限",description ="删除角色权限"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleController + UserStaticURLUtil.roleController_deleteRolePermission)
    public String deleteUserRole( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            String roleIds = params.get("roleIds");
            Integer userId = Integer.valueOf(NumberUtils.toInt(params.get("userId"), 0));
            if(roleIds == null || userId < 1){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(roleIds, Integer[].class));
                this.userService.relationOrDeleteUserRole(list,userId,currentUserId,currentCompanyId,Boolean.TRUE);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
                return gson.toJson(responseJson);
            }

        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }































//    private String newOrUpdateUserCheck(UserDto userDto ,Integer currentCompanyId) throws Exception{
//
//    }
    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
/*
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();


            return gson.toJson(responseJson);
        }catch (Exception e){
             e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }

*/






