package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.AdminUserEntity;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.IAdminUserService;
import com.zsw.services.IPermissionService;
import com.zsw.services.IRoleService;
import com.zsw.services.IUserService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/26.
 */
@RestController
@RequestMapping(UserStaticURLUtil.adminUserController)
public class AdminUserController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    IAdminUserService adminUserService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IRoleService roleService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IUserService userService;

    @RequestMapping(value=UserStaticURLUtil.adminUserController_login,
            method= RequestMethod.POST)
    public Result<HashMap<String, Object>> login(LoginTemp loginTemp) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();
        try{
            UserEntity userEntity = null;
            UserEntity paramUserEntity = new UserEntity();
            paramUserEntity.setPhone(loginTemp.getPhone());
            userEntity = this.userService.getUser(paramUserEntity);

            if(userEntity == null || userEntity.getId() == null){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("没有该后台用户");
                return result;
            }

            AdminUserEntity userAdminEntity = null;
            AdminUserEntity paramAdminUserEntity = new AdminUserEntity();
            paramAdminUserEntity.setUserId(userEntity.getId());
            userAdminEntity = this.adminUserService.getAdminUser(paramAdminUserEntity);

            if(userAdminEntity == null){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("没有该后台用户");
                return result;
            }

            if(userAdminEntity.getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("用户已被禁用");
                return result;
            }

            return UserUtils.login(this.userService,this.restTemplate,loginTemp,userEntity,Boolean.TRUE);
        }catch (Exception e){

            CommonUtils.ErrorAction(LOG,e);
            result.setCode(ResponseCode.Code_500);
            result.setMessage("系统错误");
            return result;
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminUserController_loginOut,
            method= RequestMethod.POST)
//    @AdminPermission(code = "user.adminUserController.loginOut",name = "登出管理用户",description ="登出管理用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminUserController + UserStaticURLUtil.adminUserController_loginOut)
    public String loginOut(@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return UserUtils.loginOut(this.userService,this.restTemplate,currentAdminUserId);
        } catch (Exception e) {
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminUserController_checkRememberToken,
            method= RequestMethod.POST)
    public Boolean checkRememberToken( @RequestBody Map<String,String> args) throws Exception {
        try {
            return UserUtils.checkRememberToken(this.userService,this.restTemplate,args);
        } catch (Exception e) {
            CommonUtils.ErrorAction(LOG,e);
            return Boolean.FALSE;
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminUserController_resetPassWord,
            method= RequestMethod.POST)
    public String resetPassWord(LoginTemp loginTemp) throws Exception {
        try {
            return UserUtils.resetPassWord(this.userService,this.restTemplate,loginTemp);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminUserController_getAdminUser+"/{adminUserId}",
            method= RequestMethod.GET)
//    @AdminPermission(code = "user.adminUserController.getAdminUser",name = "获取用户",description ="根据id获取用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminUserController + UserStaticURLUtil.adminUserController_getAdminUser)
    public String getAdminUser(@PathVariable Integer adminUserId) throws Exception {
        try {
            AdminUserEntity userAdminEntity = null;
            AdminUserEntity paramAdminUserEntity = new AdminUserEntity();
            paramAdminUserEntity.setUserId(adminUserId);
            userAdminEntity = this.adminUserService.getAdminUser(paramAdminUserEntity);

            if(userAdminEntity == null){
                return CommonUtils.ErrorResposeJson("没有该后台用户");
            }

            return OperationUserUtils.getUser(this.userService,this.roleService,this.permissionService,adminUserId,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminUserController_batchBan,
            method= RequestMethod.PUT)
//    @AdminPermission(code = "user.adminUserController.batchBan",name = "批量恢复/禁用后台用户",description ="批量恢复/禁用后台用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminUserController + UserStaticURLUtil.adminUserController_batchBan)
    public String batchBan( @RequestParam Map<String, String> params , @RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            String ids = params.get("ids");
            String type = params.get("type");
            if(ids == null || type == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(ids, Integer[].class));
                this.adminUserService.batchBan(list,type,currentAdminUserId);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
                return gson.toJson(responseJson);
            }

        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=UserStaticURLUtil.adminUserController_newUser,
            method= RequestMethod.POST)
//    @AdminPermission(code = "user.adminOperationUserController.newUser",name = "新增用户",description ="新增用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController_newUser)
    public String newUser(UserDto userDto, @RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationUserUtils.newUser(this.userService,this.adminUserService,userDto,currentAdminUserId,null,true);
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
