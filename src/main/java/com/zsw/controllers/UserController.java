package com.zsw.controllers;

import com.google.gson.Gson;
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
            UserEntity paramUserEntity = new UserEntity();
            //电话号码设置为参数

            UserEntity userEntity = null;

            Gson gson = new Gson();

            if(UserServiceStaticWord.loginVerifyType_passWord.equals(loginTemp.getVerifyType())){
                paramUserEntity.setPhone(loginTemp.getPhone());
                paramUserEntity.setLoginPwd(loginTemp.getPassword());
                userEntity = this.userService.getUser(paramUserEntity);
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
                paramUserEntity.setPhone(loginTemp.getPhone());
                userEntity = this.userService.getUser(paramUserEntity);
            }

            if(userEntity == null){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("账号不存在或密码错误");
            }else if(userEntity.getStatus() == CommonStaticWord.Ban_Status_1){
                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("账户禁用");
            }else{
                String rememberToken = CommonUtils.getVerifyCode_6number();

                this.userService.updateRememberToken(userEntity.getId(),rememberToken);

                Map<String, String > param = new HashMap<>();
                param.put("rememberToken",rememberToken);
                param.put("userId",userEntity.getId().toString());

                this.restTemplate.postForEntity(
                        CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                                + CacheStaticURLUtil.redisController
                                + CacheStaticURLUtil.redisController_putUserToken
                        ,param,null);

                HashMap<String,Object> data = new HashMap<>();
                userEntity.setLoginPwd(null);
                //不用返回user对象
                //data.put("user",userEntity);
                data.put("userId",userEntity.getId());
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

    @RequestMapping(value=UserStaticURLUtil.userController_loginOut,
            method= RequestMethod.POST)
//    @Permission(code = "user.userController.loginOut",name = "登出用户",description ="登出用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_loginOut)
    public String loginOut(@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            this.userService.updateRememberToken(currentUserId,null);

            Map<String, String > param = new HashMap<>();
            param.put("rememberToken","");
            param.put("userId",currentUserId.toString());

            this.restTemplate.postForEntity(
                    CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                            + CacheStaticURLUtil.redisController
                            + CacheStaticURLUtil.redisController_putUserToken
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
        Integer userId = Integer.valueOf(NumberUtils.toInt(args.get("userId"), 0));
        String rememberToken = args.get("rememberToken");
        //验证码校验
        Map<String, Object > param = new HashMap<>();
        param.put("rememberToken",rememberToken);
        param.put("userId",userId);
        ResponseEntity<Boolean> checkUserTokenResult  = this.restTemplate.postForEntity(
                CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_checkUserToken
                ,param,Boolean.class);
        if(checkUserTokenResult != null
                && checkUserTokenResult.getBody() != null
                && checkUserTokenResult.getBody()
                )return Boolean.TRUE;

        UserEntity userEntityParam = new UserEntity();
        userEntityParam.setId(userId);
        UserEntity userEntity = this.userService.getUser(userEntityParam);
        if(rememberToken != null
                && userEntity != null
                && rememberToken.equals(userEntity.getRememberToken())
                )return Boolean.TRUE;
        return Boolean.FALSE;
    }

        @RequestMapping(value=UserStaticURLUtil.userController_resetPassWord,
            method= RequestMethod.POST)
    public String resetPassWord(LoginTemp loginTemp) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            //验证码校验
            Map<String, String > paramMap = new HashMap<>();
            paramMap.put("phone",loginTemp.getPhone());
            paramMap.put("verifyCode",loginTemp.getVerifyCode());
            paramMap.put("type", CommonStaticWord.CacheServices_Redis_VerifyCode_Type_REST_PASSWORD);
            ResponseEntity<Boolean> checkVerifyCodeResult  = this.restTemplate.postForEntity(
                    CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                            + CacheStaticURLUtil.redisController
                            + CacheStaticURLUtil.redisController_checkVerifyCode
                    ,paramMap,Boolean.class);
            if(Boolean.TRUE != checkVerifyCodeResult.getBody() ){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("验证码错误");
                return gson.toJson(responseJson);
            }

            //参数校验
            String check = resetPassWordCheck(loginTemp);
            if(check != null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
                return gson.toJson(responseJson);
            }


            //重置密码
            UserEntity paramEntity = new UserEntity();
            paramEntity.setPhone(loginTemp.getPhone());
            UserEntity userEntity =this.userService.resetPassWord(paramEntity,loginTemp.getPassword());


            if(userEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("账户不存在");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("重置成功");

            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }

    }

    @RequestMapping(value=UserStaticURLUtil.userController_newUser,
            method= RequestMethod.POST)
//    @Permission(code = "user.userController.newUser",name = "新增用户",description ="新增用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_newUser)
    public String newUser(UserDto userDto,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId,Integer departmentId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            String check = OperationUserUtils.newOrUpdateUserCheck(this.userService,userDto,currentCompanyId);
            if(check != null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
                return gson.toJson(responseJson);
            }


            this.userService.newUser(userDto,currentUserId,departmentId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_getUser+"/{userId}",
            method= RequestMethod.GET)
//    @Permission(code = "user.userController.getUser",name = "获取用户",description ="根据id获取用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_getUser)
    public String getUser(@PathVariable Integer userId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationUserUtils.getUser(this.userService,userId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }



    @RequestMapping(value=UserStaticURLUtil.userController_updateUser,
            method= RequestMethod.PUT)
//    @Permission(code = "user.userController.updateUser",name = "更新用户",description ="更新用户"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_updateUser)
    public String updateUser(UserDto userDto,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationUserUtils.updateUser(this.userService,userDto,currentUserId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
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
            return CommonUtils.ErrorResposeJson();
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
            return CommonUtils.ErrorResposeJson();
        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    private String resetPassWordCheck(LoginTemp loginTemp) throws Exception{
        if(loginTemp == null)return "空信息";

        if(loginTemp.getPassword().indexOf(" ")!=-1
                )return "密码存在空格";


        if(StringUtils.isBlank(loginTemp.getPassword())
                || StringUtils.isEmpty(loginTemp.getPassword())
                ||loginTemp.getPassword().length() < 8
                ) return "密码少于8位";

        return null;
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
            Gson gson = new Gson();


            return gson.toJson(responseJson);
        }catch (Exception e){
             e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }

*/






//if(userDtoId == null){
//        if (StringUtils.isBlank(userDto.getLoginPwd())
//        ||userDto.getLoginPwd().length() < 8
//        ) return "密码少于8位";
//
//        UserEntity userEntity = new UserEntity();
//
//        userEntity.setPhone(userDto.getPhone());
//        if( this.userService.getUser(userEntity) != null
//        ) return "电话号码已存在";
//
//        userEntity.setPhone(null);
//        userEntity.setEmail(userDto.getEmail());
//        if( this.userService.getUser(userEntity) != null
//        ) return "Email地址已被使用";
//
//        userEntity.setEmail(null);
//        userEntity.setUserName(userDto.getUserName());
//        if( this.userService.getUser(userEntity) != null
//        ) return "名字已被使用";
//        }else{
//        UserEntity userEntity = new UserEntity();
//        UserEntity result = null;
//
//        userEntity.setPhone(userDto.getPhone());
//        result = this.userService.getUser(userEntity);
//        if( result != null && result.getId() != userDtoId)
//        return "电话号码已存在";
//
//        userEntity.setPhone(null);
//        userEntity.setEmail(userDto.getEmail());
//        result = this.userService.getUser(userEntity);
//        if( result != null && result.getId() != userDtoId)
//        return "Email地址已被使用";
//
//        userEntity.setEmail(null);
//        userEntity.setUserName(userDto.getUserName());
//        result = this.userService.getUser(userEntity);
//        if( result != null && result.getId() != userDtoId)
//        return "名字已被使用";
//        }