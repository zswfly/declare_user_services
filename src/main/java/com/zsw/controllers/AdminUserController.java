package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.AdminUserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.services.IAdminUserService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
    RestTemplate restTemplate;
    
    @RequestMapping(value=UserStaticURLUtil.adminUserController_login,
            method= RequestMethod.POST)
    public Result<HashMap<String, Object>> login(LoginTemp loginTemp) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();
        try{
            Gson gson = new Gson();

            AdminUserEntity paramAdminUserEntity = new AdminUserEntity();
            //电话号码设置为参数

            AdminUserEntity adminUserEntity = null;
            if(UserServiceStaticWord.loginVerifyType_passWord.equals(loginTemp.getVerifyType())){
                paramAdminUserEntity.setPhone(loginTemp.getPhone());
                paramAdminUserEntity.setLoginPwd(loginTemp.getPassword());
                adminUserEntity = this.adminUserService.getAdminUser(paramAdminUserEntity);
            }else if(UserServiceStaticWord.loginVerifyType_code.equals(loginTemp.getVerifyType())){

                //验证码校验
                Map<String, Object > param = new HashMap<>();
                param.put("phone",loginTemp.getPhone());
                param.put("verifyCode",loginTemp.getVerifyCode());
                param.put("type", CommonStaticWord.CacheServices_Redis_VerifyCode_Type_LOGIN);
                ResponseEntity<Boolean> checkVerifyCodeResult  = this.restTemplate.postForEntity(
                        CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                                + CacheStaticURLUtil.redisController
                                + CacheStaticURLUtil.redisController_checkVerifyCode
                        ,param,Boolean.class);
                if(Boolean.TRUE != checkVerifyCodeResult.getBody() ){

                    result.setCode(ResponseCode.Code_Bussiness_Error);
                    result.setMessage("验证码错误");
                    return result;
                }
                paramAdminUserEntity.setPhone(loginTemp.getPhone());
                adminUserEntity = this.adminUserService.getAdminUser(paramAdminUserEntity);
            }

            if(adminUserEntity == null){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("账号不存在或密码错误");
            }else if(adminUserEntity.getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("账户禁用");
            }else{
                String rememberToken = CommonUtils.getVerifyCode_6number();

                this.adminUserService.updateRememberToken(adminUserEntity.getId(),rememberToken);

                Map<String, String > param = new HashMap<>();
                param.put("rememberToken",rememberToken);
                param.put("userId",adminUserEntity.getId().toString());

                this.restTemplate.postForEntity(
                        CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                                + CacheStaticURLUtil.redisController
                                + CacheStaticURLUtil.redisController_putUserToken
                        ,param,null);

                HashMap<String,Object> data = new HashMap<>();
                adminUserEntity.setLoginPwd(null);
                //不用返回user对象
                //data.put("user",userEntity);
                data.put("adminUserId",adminUserEntity.getId());
                data.put("rememberToken",rememberToken);

                result.setData(data);
                result.setCode(ResponseCode.Code_200);
            }
            return result;
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
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            this.adminUserService.updateRememberToken(currentAdminUserId,null);

            Map<String, String > param = new HashMap<>();
            param.put("rememberToken","");
            param.put("adminUserId",currentAdminUserId.toString());

            this.restTemplate.postForEntity(
                    CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                            + CacheStaticURLUtil.redisController
                            + CacheStaticURLUtil.redisController_putAdminUserToken
                    ,param,null);


            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("登出成功");
            return gson.toJson(responseJson);
        } catch (Exception e) {
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.userController_checkRememberToken,
            method= RequestMethod.POST)
    public Boolean checkRememberToken( @RequestBody Map<String,String> args) throws Exception {
        Integer userId = Integer.valueOf(NumberUtils.toInt(args.get("adminUserId"), 0));
        String rememberToken = args.get("rememberToken");
        //验证码校验
        Map<String, Object > param = new HashMap<>();
        param.put("rememberToken",rememberToken);
        param.put("adminUserId",userId);
        ResponseEntity<Boolean> checkUserTokenResult  = this.restTemplate.postForEntity(
                CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_checkUserToken
                ,param,Boolean.class);
        if(checkUserTokenResult != null
                && checkUserTokenResult.getBody() != null
                && checkUserTokenResult.getBody()
                )return Boolean.TRUE;

        AdminUserEntity adminUserEntityParam = new AdminUserEntity();
        adminUserEntityParam.setId(userId);
        AdminUserEntity adminUserEntity = this.adminUserService.getAdminUser(adminUserEntityParam);
        if(rememberToken != null
                && adminUserEntity != null
                && rememberToken.equals(adminUserEntity.getRememberToken())
                )return Boolean.TRUE;
        return Boolean.FALSE;
    }






    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
