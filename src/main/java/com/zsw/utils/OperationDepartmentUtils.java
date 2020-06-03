package com.zsw.utils;

import com.google.gson.Gson;
import com.zsw.entitys.DepartmentEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.DepartmentDto;
import com.zsw.services.IDepartmentService;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/27.
 */
public class OperationDepartmentUtils {

    public static String newDepartment(IDepartmentService departmentService,DepartmentDto departmentDto, Integer currentUserId, Integer currentCompanyId) throws Exception {

        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();


        String check = newOrUpdateDepartmentCheck(departmentService,departmentDto,currentCompanyId);
        if(StringUtils.isNotEmpty(check) && StringUtils.isNotBlank(check)){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage(check);
            return gson.toJson(responseJson);
        }

        departmentService.newDepartment(departmentDto,currentUserId,currentCompanyId);

        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("新增成功");

        return gson.toJson(responseJson);
    }


    public static String updateDepartment(IDepartmentService departmentService ,DepartmentDto departmentDto, Integer currentUserId, Integer currentCompanyId) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();


        String check = newOrUpdateDepartmentCheck(departmentService ,departmentDto,currentCompanyId);
        if(StringUtils.isNotEmpty(check) && StringUtils.isNotBlank(check)){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage(check);
            return gson.toJson(responseJson);
        }

        departmentService.updateDepartment(departmentDto,currentUserId,currentCompanyId);

        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("更新成功");

        return gson.toJson(responseJson);

    }

    public static String updateStatusDepartment(IDepartmentService departmentService ,Map<String, String> params ,Integer currentUserId,Integer currentCompanyId) throws Exception {
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
            departmentService.updateStatusDepartment(list,type,currentUserId,currentCompanyId);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");
            return gson.toJson(responseJson);
        }
    }

    public static String getDepartment(IDepartmentService departmentService ,Integer departmentId, Integer currentCompanyId) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();

        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(departmentId);

        if(currentCompanyId != null && currentCompanyId > 0)
            departmentEntity.setCompanyId(currentCompanyId);

        departmentEntity = departmentService.getDepartment(departmentEntity);

        if(departmentEntity == null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage("没该id部门");
        }else{
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setData(departmentEntity);
        }

        return gson.toJson(responseJson);
    }


    public static String departmentsPage(IDepartmentService departmentService ,NativeWebRequest request, Integer currentCompanyId) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();
        Map<String,Object> paramMap = new HashMap<String, Object>();

        if(currentCompanyId != null && currentCompanyId > 0){
            paramMap.put("companyId", currentCompanyId);
        }else{
            String companyId = request.getParameter("companyId");
            if(companyId !=null && StringUtils.isNotEmpty(companyId)) {
                paramMap.put("companyId", Integer.valueOf(NumberUtils.toInt(companyId, 0)));
            }
        }

        String status = request.getParameter("status");
        if(status !=null && StringUtils.isNotEmpty(status)) {
            paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
        }
        String departmentName = request.getParameter("departmentName");
        if(departmentName !=null && StringUtils.isNotEmpty(departmentName)) {
            paramMap.put("departmentName", departmentName);
        }

        String mnemonicCode = request.getParameter("mnemonicCode");
        if(mnemonicCode !=null && StringUtils.isNotEmpty(mnemonicCode)) {
            paramMap.put("mnemonicCode", mnemonicCode);
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
        List<DepartmentEntity> items = departmentService.listDepartmentEntity(paramMap);
        Integer total = departmentService.listDepartmentEntityCount(paramMap);
        data.put("items",items);
        data.put("total",total==null?0:total);
        responseJson.setData(data);
        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("搜索成功");

        return gson.toJson(responseJson);
    }














        private static String newOrUpdateDepartmentCheck(IDepartmentService departmentService,DepartmentDto departmentDto , Integer currentCompanyId) throws Exception {
        if (StringUtils.isBlank(departmentDto.getName())
                || StringUtils.isEmpty(departmentDto.getName())
                ) return "部门名有误";


        if (departmentDto.getName().indexOf(",")!=-1
                || departmentDto.getName().indexOf(" ")!=-1
                ) return "部门名有空格或,号";

        String resutl = departmentService.checkDepartmentExist(departmentDto,currentCompanyId);
        if(StringUtils.isNotEmpty(resutl) && StringUtils.isNotBlank(resutl))
            return resutl;

        return null;
    }

}
