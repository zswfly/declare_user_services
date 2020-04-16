package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.services.IUserService;
import com.zsw.utils.CacheStaticURLUtil;
import com.zsw.utils.UserServiceStaticWord;
import com.zsw.utils.UserStaticURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
@Controller
@RequestMapping(UserStaticURLUtil.userController)
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping(value=UserStaticURLUtil.userController_login,
            method= RequestMethod.POST)
    @ResponseBody
    public String login(LoginTemp loginTemp) throws Exception {
        try{
            UserEntity param = new UserEntity();
            param.setPhone(loginTemp.getPhone());
            UserEntity result = null;
            if(UserServiceStaticWord.loginVerifyType_passWord.equals(loginTemp.getVerifyType())){
                param.setLoginPwd(loginTemp.getPassWord());
                result = this.userService.getUser(param);
                result.setLoginPwd("");
            }else if(UserServiceStaticWord.loginVerifyType_code.equals(loginTemp.getVerifyType())){
                "55".equals(loginTemp.getVerifyCode());
                result = this.userService.getUser(param);
                result.setLoginPwd("");
            }
            ResponseJson responseJson = new ResponseJson();
            responseJson.setCode("200");
            if(result == null){
                responseJson.setMessage("账号不存在或密码错误");
            }else{
                Map<String,Object> data = new HashMap<>();
                data.put("user",result);
                UUID token = UUID.randomUUID();
                data.put("token",token);
                Map<String,Object> tokenMap = new HashMap<>();
                tokenMap.put("userId",result.getId()+"");
                tokenMap.put("token",token);
                this.restTemplate.postForEntity(
                        "http://cache-services"
                                + CacheStaticURLUtil.redisController
                                + CacheStaticURLUtil.redisController_setToken
                        ,tokenMap,Integer.class);
                responseJson.setData(data);
            }
            Gson gson = new Gson();
            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();

            ResponseJson responseJson = new ResponseJson();
            responseJson.setMessage("系统错误,请联络工作人员");
            Gson gson = new Gson();
            return gson.toJson(responseJson);

        }

    }

}
