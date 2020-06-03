package com.zsw.utils;

import com.google.gson.Gson;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.common.Result;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.services.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/29.
 */
public class UserUtils {
    public static Result<HashMap<String, Object>> login(IUserService userService , RestTemplate restTemplate , LoginTemp loginTemp, UserEntity userEntity) throws Exception {
        Result<HashMap<String, Object>> result= new Result<HashMap<String, Object>>();

        Gson gson = new Gson();

        if(UserServiceStaticWord.loginVerifyType_passWord.equals(loginTemp.getVerifyType())){
            if(userEntity == null || loginTemp == null || userEntity.getLoginPwd() == null || !userEntity.getLoginPwd().equals(loginTemp.getPassword()))
                userEntity = null ;
        }else if(UserServiceStaticWord.loginVerifyType_code.equals(loginTemp.getVerifyType())){

            //验证码校验
            Map<String, Object > param = new HashMap<>();
            param.put("phone",loginTemp.getPhone());
            param.put("verifyCode",loginTemp.getVerifyCode());
            param.put("type", CommonStaticWord.CacheServices_Redis_VerifyCode_Type_LOGIN);
            ResponseEntity<Boolean> checkVerifyCodeResult  = restTemplate.postForEntity(
                    CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                            + CacheStaticURLUtil.redisController
                            + CacheStaticURLUtil.redisController_checkVerifyCode
                    ,param,Boolean.class);
            if(Boolean.TRUE != checkVerifyCodeResult.getBody() ){

                result.setCode(ResponseCode.Code_Bussiness_Error);
                result.setMessage("验证码错误");
                return result;
            }
        }

        if(userEntity == null){
            result.setCode(ResponseCode.Code_Bussiness_Error);
            result.setMessage("账号不存在或密码错误");
        }else{
            String rememberToken = CommonUtils.getVerifyCode_6number();

            userService.updateRememberToken(userEntity.getId(),rememberToken);

            Map<String, String > param = new HashMap<>();
            param.put("rememberToken",rememberToken);
            param.put("userId",userEntity.getId().toString());

            restTemplate.postForEntity(
                    CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                            + CacheStaticURLUtil.redisController
                            + CacheStaticURLUtil.redisController_putUserToken
                    ,param,null);

            HashMap<String,Object> data = new HashMap<>();

            //不用返回user对象
            //userEntity.setLoginPwd(null);
            //data.put("user",userEntity);
            data.put("userId",userEntity.getId());
            data.put("rememberToken",rememberToken);

            result.setData(data);
            result.setCode(ResponseCode.Code_200);
        }
        return result;
    }






    public static String loginOut(IUserService userService , RestTemplate restTemplate ,  Integer currentUserId) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = new Gson();
        userService.updateRememberToken(currentUserId,null);

        Map<String, String > param = new HashMap<>();
        param.put("rememberToken","");
        param.put("userId",currentUserId.toString());

        restTemplate.postForEntity(
                CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_putUserToken
                ,param,null);

        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("登出成功");
        return gson.toJson(responseJson);

    }


    public static Boolean checkRememberToken(IUserService userService , RestTemplate restTemplate ,  Map<String,String> args) throws Exception {
        Integer userId = Integer.valueOf(NumberUtils.toInt(args.get("userId"), 0));
        String rememberToken = args.get("rememberToken");
        //验证码校验
        Map<String, Object > param = new HashMap<>();
        param.put("rememberToken",rememberToken);
        param.put("userId",userId);
        ResponseEntity<Boolean> checkUserTokenResult  = restTemplate.postForEntity(
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
        UserEntity userEntity = userService.getUser(userEntityParam);
        if(rememberToken != null
                && userEntity != null
                && rememberToken.equals(userEntity.getRememberToken())
                )return Boolean.TRUE;
        return Boolean.FALSE;
    }



    public static String resetPassWord(IUserService userService , RestTemplate restTemplate , LoginTemp loginTemp) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = new Gson();

        //验证码校验
        Map<String, String > paramMap = new HashMap<>();
        paramMap.put("phone",loginTemp.getPhone());
        paramMap.put("verifyCode",loginTemp.getVerifyCode());
        paramMap.put("type", CommonStaticWord.CacheServices_Redis_VerifyCode_Type_REST_PASSWORD);
        ResponseEntity<Boolean> checkVerifyCodeResult  = restTemplate.postForEntity(
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
        UserEntity userEntity = userService.resetPassWord(paramEntity,loginTemp.getPassword());


        if(userEntity == null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage("账户不存在");
        }else{
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("重置成功");

        }

        return gson.toJson(responseJson);
    }



    private static String resetPassWordCheck(LoginTemp loginTemp) throws Exception{
        if(loginTemp == null)return "空信息";

        if(loginTemp.getPassword().indexOf(" ")!=-1
                )return "密码存在空格";


        if(StringUtils.isBlank(loginTemp.getPassword())
                || StringUtils.isEmpty(loginTemp.getPassword())
                ||loginTemp.getPassword().length() < 8
                ) return "密码少于8位";

        return null;
    }
}
