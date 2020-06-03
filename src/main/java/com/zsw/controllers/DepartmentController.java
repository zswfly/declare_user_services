package com.zsw.controllers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsw.controller.BaseController;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.DepartmentUserEntity;
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

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentController.class);


    public static void main(String[] args) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setMnemonicCode("123123");
        System.out.println("-------------------------");
        System.out.println(gson.toJson(departmentEntity));
        System.out.println("-------------------------");
    }



    @RequestMapping(value=UserStaticURLUtil.departmentController_newDepartment,
            method= RequestMethod.POST)
    //    @Permission(code = "user.departmentController.newDepartment",name = "新增部门",description ="新增部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.companyController_newDepartment)
    public String newDepartment(DepartmentDto departmentDto, @RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationDepartmentUtils.newDepartment(this.departmentService,departmentDto,currentUserId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=UserStaticURLUtil.departmentController_updateDepartment,
            method= RequestMethod.PUT)
    //    @Permission(code = "user.departmentController.updateDepartment",name = "更新部门",description ="更新部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_updateDepartment)
    public String updateDepartment(DepartmentDto departmentDto,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationDepartmentUtils.updateDepartment(this.departmentService,departmentDto,currentUserId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.departmentController_updateStatusDepartment,
            method= RequestMethod.PUT)
    //    @Permission(code = "user.departmentController.updateStatusDepartment",name = "部门",description ="删除(禁用)部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_updateStatusDepartment)
    public String updateStatusDepartment(@RequestParam Map<String, String> params ,@RequestHeader("userId") Integer currentUserId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationDepartmentUtils.updateStatusDepartment(this.departmentService,params,currentUserId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.departmentController_getDepartment+"/{departmentId}",
            method= RequestMethod.GET)
//    @Permission(code = "user.departmentController.getDepartment",name = "获取部门",description ="根据id获取部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_getDepartment)
    public String getDepartment(@PathVariable Integer departmentId,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationDepartmentUtils.getDepartment(this.departmentService , departmentId,currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=UserStaticURLUtil.departmentController_departmentsPage,
            method= RequestMethod.GET)
//    @Permission(code = "user.departmentController.departmentsPage",name = "搜索部门",description ="搜索部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_departmentsPage)
    public String departmentsPage(NativeWebRequest request,@RequestHeader("companyId") Integer currentCompanyId) throws Exception {
        try {
            return OperationDepartmentUtils.departmentsPage(this.departmentService ,request, currentCompanyId);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=UserStaticURLUtil.departmentController_relationDepartmentUser,
            method= RequestMethod.GET)
    //    @Permission(code = "user.departmentController.relationDepartmentUser",name = "关联用户与部门",description ="关联用户与部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_relationDepartmentUser)
    public String relationDepartmentUser(String jsonDepartmentUser,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
//            2.把jsonList转化为一个list对象
//            String jsonList="[{'userid':'1881140130','username':'三哥','usersex':'男','banji':'计算机1班','phone':'18255052351'},"
//                    + "{'userid':'1881140131','username':'蜂','usersex':'男','banji':'计算机1班','phone':'18355092351'},"
//                    + "{'userid':'1881140132','username':'宝','usersex':'男','banji':'计算机1班','phone':'18955072351'}]";
            List<DepartmentUserEntity> listDepartmentUser= gson.fromJson(jsonDepartmentUser, new TypeToken<List<DepartmentUserEntity>>() {}.getType());

            this.departmentService.relationDepartmentUser(listDepartmentUser,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("操作成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }
    @RequestMapping(value=UserStaticURLUtil.departmentController_deleteDepartmentUser,
            method= RequestMethod.DELETE)
    //    @Permission(code = "user.departmentController.deleteDepartmentUser",name = "取消关联用户与部门",description ="取消(删除)关联用户与部门"
//            ,url=CommonStaticWord.userServices + UserStaticURLUtil.departmentController + UserStaticURLUtil.departmentController_deleteDepartmentUser)
    public String deleteDepartmentUser(String jsonDepartmentUser,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
//            2.把jsonList转化为一个list对象
//            String jsonList="[{'userid':'1881140130','username':'三哥','usersex':'男','banji':'计算机1班','phone':'18255052351'},"
//                    + "{'userid':'1881140131','username':'蜂','usersex':'男','banji':'计算机1班','phone':'18355092351'},"
//                    + "{'userid':'1881140132','username':'宝','usersex':'男','banji':'计算机1班','phone':'18955072351'}]";
            List<DepartmentUserEntity> listDepartmentUser= gson.fromJson(jsonDepartmentUser, new TypeToken<List<DepartmentUserEntity>>() {}.getType());

            this.departmentService.deleteDepartmentUser(listDepartmentUser,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("操作成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    private String newOrUpdateDepartmentCheck(DepartmentDto departmentDto ,Integer currentCompanyId) throws Exception {
        if (StringUtils.isBlank(departmentDto.getName())
                || StringUtils.isEmpty(departmentDto.getName())
                ) return "部门名有误";


        if (departmentDto.getName().indexOf(",")!=-1
                || departmentDto.getName().indexOf(" ")!=-1
                ) return "部门名有空格或,号";

        String resutl = this.departmentService.checkDepartmentExist(departmentDto,currentCompanyId);
        if(StringUtils.isNotEmpty(resutl) && StringUtils.isNotBlank(resutl))
            return resutl;

        return null;
    }



    @Override
    public Logger getLOG(){
        return this.LOG;
    }
}
