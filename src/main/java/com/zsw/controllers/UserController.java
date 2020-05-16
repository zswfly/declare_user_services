package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IUserService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value=UserStaticURLUtil.userController_login,
            method= RequestMethod.POST)
    public Result<HashMap<String, Object>> login(LoginTemp loginTemp) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();
        try{
            UserEntity paramUserEntity = new UserEntity();
            //电话号码设置为参数

            UserEntity userEntity = null;

            //ResponseJson responseJson = new ResponseJson();
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
                HashMap<String,Object> data = new HashMap<>();
                userEntity.setLoginPwd(null);
                //不用返回user对象
                //data.put("user",userEntity);
                data.put("userId",userEntity.getId());
                result.setData(data);
                result.setCode(ResponseCode.Code_200);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResponseCode.Code_500);
            result.setMessage("系统错误");
            return result;
        }

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
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }

    }

    @RequestMapping(value=UserStaticURLUtil.userController_newUser,
            method= RequestMethod.POST)
    public String newUser(UserDto userDto) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            String check = newOrUpdateUserCheck(userDto);
            if(check != null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
                return gson.toJson(responseJson);
            }

            UserEntity result = this.userService.newUser(userDto);

            if(result == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("操作失败");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("新增成功");
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_getUser+"/{userId}",
            method= RequestMethod.GET)
    public String getUser(@PathVariable Integer userId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            userEntity = this.userService.getUser(userEntity);

            if(userEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("没有用户");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                userEntity.setLoginPwd(null);
                responseJson.setData(userEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }



    @RequestMapping(value=UserStaticURLUtil.userController_updateUser,
            method= RequestMethod.POST)
    public String updateUser(UserDto userDto) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            String check = newOrUpdateUserCheck(userDto);
            if(check != null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
                return gson.toJson(responseJson);
            }

            UserEntity userEntity = this.userService.updateUser(userDto);

            if(userEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("更新失败");
            }else{

                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "user.userController.batchBan",name = "批量禁用/恢复",description ="批量禁用/恢复用户"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_batchBan)
    public String batchBan(List<Integer> ids,String type,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.userService.batchBan(ids,type,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.userController_usersPage,
            method= RequestMethod.GET)
    //@Permission(code = "user.userController.usersPage",name = "条件搜索用户",description ="条件搜索用户"
    //        ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_usersPage)
    public String usersPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotBlank(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String departmentId = request.getParameter("departmentId");
            if(departmentId !=null && StringUtils.isNotBlank(departmentId)) {
                paramMap.put("departmentId", Integer.valueOf(NumberUtils.toInt(departmentId, 0)));
            }
            String phone = request.getParameter("phone");
            if(phone !=null && StringUtils.isNotBlank(phone)) {
                paramMap.put("phone", phone);
            }
            String email = request.getParameter("email");
            if(email !=null && StringUtils.isNotBlank(email)) {
                paramMap.put("email", email);
            }
            String userName = request.getParameter("userName");
            if(userName !=null && StringUtils.isNotBlank(userName)) {
                paramMap.put("userName", userName);
            }
            String beginCreateTime = request.getParameter("beginCreateTime");
            if(beginCreateTime !=null && StringUtils.isNotBlank(beginCreateTime)) {
                paramMap.put("beginCreateTime", beginCreateTime);
            }
            String endCreateTime = request.getParameter("endCreateTime");
            if(endCreateTime !=null && StringUtils.isNotBlank(endCreateTime)) {
                paramMap.put("endCreateTime", endCreateTime);
            }

            Integer currentPage = Integer.valueOf(NumberUtils.toInt(request.getParameter("currentPage"), 1));
            Integer pageSize = Integer.valueOf(NumberUtils.toInt(request.getParameter("pageSize"), 10));

            paramMap.put("start", (currentPage-1)*pageSize);
            paramMap.put("pageSize", pageSize);


            Map<String,Object> data = new HashMap<>();
            List<UserDto> items = this.userService.usersPage(paramMap);
            data.put("items",items);
            data.put("total",items==null?0:items.size());
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }


    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    private String resetPassWordCheck(LoginTemp loginTemp) throws Exception{
        if(loginTemp == null)return "空信息";

        if(StringUtils.isBlank(loginTemp.getPassword())
                ||loginTemp.getPassword().length() < 8
                ) return "密码少于8位";

        return null;
    }


    private String newOrUpdateUserCheck(UserDto userDto ) throws Exception{
        if(userDto == null)return "空信息";

        if (StringUtils.isBlank(userDto.getLoginPwd())
                ||userDto.getLoginPwd().length() < 8
                ) return "密码少于8位";

        if (StringUtils.isBlank(userDto.getPhone())
                ||!CommonUtils.isMobileNO(userDto.getPhone())
                ) return "手机号码有误";


        if (StringUtils.isBlank(userDto.getEmail())
                ||!CommonUtils.isEmail(userDto.getEmail())
                ) return "Email地址有误";


        if (StringUtils.isBlank(userDto.getUserName())
                ) return "用户名有误";


        UserEntity userEntity = new UserEntity();

        userEntity.setPhone(userDto.getPhone());
        if( this.userService.getUser(userEntity) != null
                ) return "电话号码已存在";

        userEntity.setPhone(null);
        userEntity.setEmail(userDto.getEmail());
        if( this.userService.getUser(userEntity) != null
                ) return "Email地址已被使用";

        userEntity.setEmail(null);
        userEntity.setUserName(userDto.getUserName());
        if( this.userService.getUser(userEntity) != null
                ) return "名字已被使用";

        return null;
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
