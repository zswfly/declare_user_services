package com.zsw.controllers;

import com.zsw.controller.BaseController;
import com.zsw.controller.IUserPermissionController;
import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;
import com.zsw.services.IPermissionService;
import com.zsw.utils.CacheStaticURLUtil;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.UserStaticURLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by zhangshaowei on 2020/4/13.
 */
@RestController
@RequestMapping(UserStaticURLUtil.permissionController)
public class PermissionController extends BaseController implements IUserPermissionController{
    @Autowired
    IPermissionService permissionService;

    @Autowired
    RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(PermissionController.class);



    @RequestMapping(value=UserStaticURLUtil.permissionController_initPermission,
            method= RequestMethod.POST)
    public void initPermission(@RequestBody List<InitPermission> initPermissionList) throws Exception{

        //1.跟新权限数据
        this.permissionService.initPermission(initPermissionList);

        //2.缓存更新个人权限
        List<UserPermission> userPermissionList = this.permissionService.listUserPermission(null);
        Map<Integer,Set<String>> userPermissions = new HashMap<>();
        for(UserPermission userPermission :userPermissionList){
            if(userPermissions.containsKey(userPermission.getUserId())){
                userPermissions.get(userPermission.getUserId()).add(userPermission.getPermissionCode());
            }else{
                Set<String> tempSet = new HashSet<>();
                tempSet.add(userPermission.getPermissionCode());
                userPermissions.put(userPermission.getUserId(),tempSet);
            }
        }
        this.restTemplate.postForEntity(
                CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_initPermission
                ,userPermissions,Integer.class);



    }

    @Override
    public Logger getLOG(){
        return this.LOG;
    }
}
