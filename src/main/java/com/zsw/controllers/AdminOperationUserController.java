package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IUserService;
import com.zsw.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/26.
 */
@RestController
@RequestMapping(UserStaticURLUtil.adminOperationUserController)
public class AdminOperationUserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminOperationUserController.class);

    @Autowired
    IUserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ICompanyService companyService;

    @RequestMapping(value=UserStaticURLUtil.adminOperationUserController_newUser,
            method= RequestMethod.POST)
//    @AdminPermission(code = "user.adminOperationUserController.newUser",name = "新增用户",description ="新增用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController_newUser)
    public String newUser(UserDto userDto, @RequestHeader("adminUserId") Integer currentAdminUserId,  Integer departmentId) throws Exception {
        try {
            return OperationUserUtils.newUser(this.userService,null,userDto,currentAdminUserId,departmentId,false);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminOperationUserController_getUser+"/{userId}",
            method= RequestMethod.GET)
//    @AdminPermission(code = "user.adminOperationUserController.getUser",name = "获取用户",description ="根据id获取用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController_getUser)
    public String getUser(@PathVariable Integer userId) throws Exception {
        try {
            return OperationUserUtils.getUser(this.userService,userId,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminOperationUserController_updateUser,
            method= RequestMethod.PUT)
//    @AdminPermission(code = "user.adminOperationUserController.updateUser",name = "更新用户",description ="更新用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController_updateUser)
    public String updateUser(UserDto userDto,@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationUserUtils.updateUser(this.userService,userDto,currentAdminUserId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminOperationUserController_batchBan,
            method= RequestMethod.PUT)
    //@AdminPermission(code = "user.adminOperationUserController.batchBan",name = "批量禁用/恢复",description ="批量禁用/恢复用户"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController_batchBan)
    public String batchBan(@RequestParam Map<String, String> params , @RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationUserUtils.batchBan(this.userService,params,currentAdminUserId,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }
    @RequestMapping(value=UserStaticURLUtil.userController_usersPage,
            method= RequestMethod.GET)
    //@AdminPermission(code = "user.adminOperationUserController.usersPage",name = "条件搜索用户",description ="条件搜索用户"
    //        ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationUserController + UserStaticURLUtil.adminOperationUserController)
    public String usersPage(NativeWebRequest request) throws Exception {
        try {
            return OperationUserUtils.usersPage(this.userService,request,null);
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
