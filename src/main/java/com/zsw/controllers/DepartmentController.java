package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.DepartmentDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IDepartmentService;
import com.zsw.services.IUserService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.ResponseCode;
import com.zsw.utils.UserStaticURLUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/21.
 */
@RestController
@RequestMapping(UserStaticURLUtil.departmentController)
public class DepartmentController extends BaseController {

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ICompanyService companyService;

    @RequestMapping(value=UserStaticURLUtil.departmentController_newDepartment,
            method= RequestMethod.POST)
    //    @Permission(code = "user.departmentController.newDepartment",name = "新增部门",description ="新增部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.companyController_newDepartment)
    public String newDepartment(DepartmentDto departmentDto, @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.departmentService.newDepartment(departmentDto,currentUserId,currentCompanyId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }

    @RequestMapping(value=UserStaticURLUtil.departmentController_deleteDepartment,
            method= RequestMethod.DELETE)
    //    @Permission(code = "user.departmentController.deleteDepartment",name = "删除部门",description ="删除(禁用)部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_deleteDepartment)
    public String deleteDepartment(Integer departmentId,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.departmentService.deleteDepartment(departmentId,currentUserId,currentCompanyId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("删除成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.departmentController_updateDepartment,
            method= RequestMethod.DELETE)
    //    @Permission(code = "user.departmentController.updateDepartment",name = "更新部门",description ="更新部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_updateDepartment)
    public String updateDepartment(DepartmentDto departmentDto,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            this.departmentService.updateDepartment(departmentDto,currentUserId,currentCompanyId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }


    @RequestMapping(value=UserStaticURLUtil.departmentController_getDepartment+"/{departmentId}",
            method= RequestMethod.GET)
//    @Permission(code = "user.departmentController.getDepartment",name = "获取部门",description ="根据id获取部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_getDepartment)
    public String getDepartment(@PathVariable Integer departmentId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            DepartmentEntity departmentEntity = new DepartmentEntity();
            departmentEntity.setId(departmentId);
            departmentEntity.setCompanyId(currentCompanyId);

            departmentEntity = this.departmentService.getDepartment(departmentEntity);

            if(departmentEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("没该id部门");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(departmentEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }




    @RequestMapping(value=UserStaticURLUtil.departmentController_departmentsPage,
            method= RequestMethod.GET)
//    @Permission(code = "user.departmentController.departmentsPage",name = "搜索部门",description ="搜索部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_departmentsPage)
    public String departmentsPage(NativeWebRequest request,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            paramMap.put("companyId",currentCompanyId);

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotBlank(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String departmentName = request.getParameter("departmentName");
            if(departmentName !=null && StringUtils.isNotBlank(departmentName)) {
                paramMap.put("departmentName", departmentName);
            }

            String mnemonicCode = request.getParameter("mnemonicCode");
            if(mnemonicCode !=null && StringUtils.isNotBlank(mnemonicCode)) {
                paramMap.put("mnemonicCode", mnemonicCode);
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
            List<DepartmentEntity> items = this.departmentService.listDepartmentEntity(paramMap);
            Integer total = this.departmentService.listDepartmentEntityCount(paramMap);
            data.put("items",items);
            data.put("total",total==null?0:total);
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }







}
