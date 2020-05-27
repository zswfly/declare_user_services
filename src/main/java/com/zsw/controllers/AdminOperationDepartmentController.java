package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.DepartmentDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IDepartmentService;
import com.zsw.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/26.
 */
@RestController
@RequestMapping(UserStaticURLUtil.adminOperationDepartmentController)
public class AdminOperationDepartmentController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminOperationDepartmentController.class);

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ICompanyService companyService;


    @RequestMapping(value=UserStaticURLUtil.adminOperationDepartmentController_newDepartment,
            method= RequestMethod.POST)
    //    @AdminPermission(code = "user.adminOperationDepartmentController.newDepartment",name = "新增部门",description ="新增部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationDepartmentController + UserStaticURLUtil.adminOperationDepartmentController_newDepartment)
    public String newDepartment(DepartmentDto departmentDto, @RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationDepartmentUtils.newDepartment(this.departmentService,departmentDto,0,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminOperationDepartmentController_updateDepartment,
            method= RequestMethod.PUT)
    //    @AdminPermission(code = "user.adminOperationDepartmentController.updateDepartment",name = "更新部门",description ="更新部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationDepartmentController + UserStaticURLUtil.adminOperationDepartmentController_updateDepartment)
    public String updateDepartment(DepartmentDto departmentDto,@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationDepartmentUtils.updateDepartment(this.departmentService,departmentDto,0,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.adminOperationDepartmentController_updateStatusDepartment,
            method= RequestMethod.PUT)
    //    @AdminPermission(code = "user.adminOperationDepartmentController.updateStatusDepartment",name = "部门",description ="删除(禁用)部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationDepartmentController + UserStaticURLUtil.adminOperationDepartmentController_updateStatusDepartment)
    public String updateStatusDepartment(@RequestParam Map<String, String> params ,@RequestHeader("adminUserId") Integer currentAdminUserId) throws Exception {
        try {
            return OperationDepartmentUtils.updateStatusDepartment(this.departmentService,params,0,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.adminOperationDepartmentController_getDepartment+"/{departmentId}",
            method= RequestMethod.GET)
//    @AdminPermission(code = "user.adminOperationDepartmentController.getDepartment",name = "获取部门",description ="根据id获取部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.adminOperationDepartmentController + UserStaticURLUtil.adminOperationDepartmentController_getDepartment)
    public String getDepartment(@PathVariable Integer departmentId) throws Exception {
        try {
            return OperationDepartmentUtils.getDepartment(this.departmentService , departmentId,0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.departmentController_departmentsPage,
            method= RequestMethod.GET)
//    @Permission(code = "user.departmentController.departmentsPage",name = "搜索部门",description ="搜索部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_departmentsPage)
    public String departmentsPage(NativeWebRequest request ) throws Exception {
        try {
            return OperationDepartmentUtils.departmentsPage(this.departmentService ,request, 0);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson();
        }
    }


    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
