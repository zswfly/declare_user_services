package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.controller.IUserPermissionController;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.PermissionEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.InitPermission;
import com.zsw.entitys.user.UserPermission;
import com.zsw.services.IPermissionService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

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

    @RequestMapping(value=UserStaticURLUtil.permissionController_permissionsPage,
            method= RequestMethod.GET)
//    @Permission(code = "user.permissionController.permissionsPage",name = "搜索权限",description ="搜索权限"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.permissionController + UserStaticURLUtil.permissionController_permissionsPage)
    public String permissionsPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String parentId = request.getParameter("parentId");
            if(parentId !=null && StringUtils.isNotEmpty(parentId)) {
                paramMap.put("parentId", Integer.valueOf(NumberUtils.toInt(parentId, CommonStaticWord.Normal_Status_0)));
            }
            String permissionName = request.getParameter("permissionName");
            if(permissionName !=null && StringUtils.isNotEmpty(permissionName)) {
                paramMap.put("permissionName", permissionName);
            }

            String code = request.getParameter("code");
            if(code !=null && StringUtils.isNotEmpty(code)) {
                paramMap.put("code", code);
            }

            String beginCreateTime = request.getParameter("beginCreateTime");
            if(beginCreateTime !=null && StringUtils.isNotEmpty(beginCreateTime)) {
                paramMap.put("beginCreateTime", beginCreateTime);
            }
            String endCreateTime = request.getParameter("endCreateTime");
            if(endCreateTime !=null && StringUtils.isNotEmpty(endCreateTime)) {
                paramMap.put("endCreateTime", endCreateTime);
            }


            Integer currentPage = Integer.valueOf(NumberUtils.toInt(request.getParameter("currentPage"), 1));
            Integer pageSize = Integer.valueOf(NumberUtils.toInt(request.getParameter("pageSize"), 10));

            paramMap.put("start", (currentPage-1)*pageSize);
            paramMap.put("pageSize", pageSize);

            Map<String,Object> data = new HashMap<>();
            List<PermissionEntity> items = this.permissionService.listPermissionEntity(paramMap);

            Integer total = this.permissionService.listPermissionEntityCount(paramMap);
            data.put("items",items);
            data.put("total",total==null?0:total);
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=UserStaticURLUtil.permissionController_getPermission+"/{permissionId}",
            method= RequestMethod.GET)
//    @Permission(code = "user.permissionController.getPermission",name = "获取权限",description ="根据id获取权限"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.permissionController + UserStaticURLUtil.permissionController_getPermission)
    public String getPermission(@PathVariable Integer permissionId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setId(permissionId);
            permissionEntity = this.permissionService.getPermission(permissionEntity);
            if(permissionEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没权限");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(permissionEntity);
            }

            return gson.toJson(responseJson);
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
