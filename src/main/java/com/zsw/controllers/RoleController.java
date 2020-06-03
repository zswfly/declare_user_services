package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.RoleEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.IRoleService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/6/2.
 */
@RestController
@RequestMapping(UserStaticURLUtil.roleController)
public class RoleController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    IRoleService roleService;
    
    public String newRole(RoleEntity roleEntity, @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            String check =this.roleService.checkRoleExist(roleEntity, currentCompanyId);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.roleService.newRole(roleEntity,currentUserId, currentCompanyId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value= UserStaticURLUtil.roleControler_updateRole,
            method= RequestMethod.PUT)
    //    @Permission(code = "user.roleControler.updateRole",name = "更新角色",description ="更新角色"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleControler + UserStaticURLUtil.roleControler_updateRole)
    public String updateRole(RoleEntity roleEntity,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            String check =this.roleService.checkRoleExist(roleEntity,currentCompanyId);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.roleService.updateRole(roleEntity,currentUserId, currentCompanyId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=UserStaticURLUtil.roleControler_getRole+"/{roleId}",
            method= RequestMethod.GET)
    //    @Permission(code = "user.roleControler.getRole",name = "获取单个角色",description ="获取单个角色"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleControler + UserStaticURLUtil.roleControler_getRole)
    public String getRole(@PathVariable Integer roleId,Integer currentCompanyId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(roleId);
            roleEntity.setCompanyId(currentCompanyId);
            roleEntity = this.roleService.getRole(roleEntity);
            if(roleEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没角色");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(roleEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.roleControler_rolePage,
            method= RequestMethod.GET)
    //    @Permission(code = "user.roleControler.rolePage",name = "搜索角色",description ="搜索角色"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleControler + UserStaticURLUtil.roleControler_rolePage)
    public String rolePage(NativeWebRequest request,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            paramMap.put("currentCompanyId", currentCompanyId);

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String roleName = request.getParameter("roleName");
            if(roleName !=null && StringUtils.isNotEmpty(roleName)) {
                paramMap.put("roleName", roleName);
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
            List<RoleEntity> items = this.roleService.listRoleEntity(paramMap);
            Integer total = this.roleService.listRoleEntityCount(paramMap);
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



    @RequestMapping(value=UserStaticURLUtil.roleControler_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "user.roleControler.batchBan",name = "批量禁用/恢复角色",description ="批量禁用/恢复角色"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleControler + UserStaticURLUtil.roleControler_batchBan)
    public String batchBan( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
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
                this.roleService.batchBan(list,type,currentUserId,currentCompanyId);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
                return gson.toJson(responseJson);
            }

        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }







//    @RequestMapping(value=UserStaticURLUtil.roleControler_relationRolePermission,
//            method= RequestMethod.PUT)
//    //@Permission(code = "user.roleControler.relationRolePermission",name = "关联角色权限",description ="批量禁用/恢复角色"
//    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.roleControler + UserStaticURLUtil.roleControler_relationRolePermission)
//    public String relationRolePermission( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
//        try {
//            ResponseJson responseJson = new ResponseJson();
//            Gson gson = CommonUtils.getGson();
//            String ids = params.get("ids");
//            String type = params.get("type");
//            if(ids == null || type == null){
//                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
//                responseJson.setMessage("参数不全");
//                return gson.toJson(responseJson);
//            }else{
//                List<Integer> list = Arrays.asList(gson.fromJson(ids, Integer[].class));
//                this.roleService.batchBan(list,type,currentUserId,currentCompanyId);
//                responseJson.setCode(ResponseCode.Code_200);
//                responseJson.setMessage("更新成功");
//                return gson.toJson(responseJson);
//            }
//
//        }catch (Exception e){
//            CommonUtils.ErrorAction(LOG,e);
//            return CommonUtils.ErrorResposeJson(null);
//        }
//    }










    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
