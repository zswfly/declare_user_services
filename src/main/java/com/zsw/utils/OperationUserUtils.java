package com.zsw.utils;

import com.google.gson.Gson;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.IAdminUserService;
import com.zsw.services.IUserService;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/27.
 */
public class OperationUserUtils {
    public static String newOrUpdateUserCheck(IUserService userService, UserDto userDto /*, Integer currentCompanyId,Integer departmentId*/) throws Exception{
        if(userDto == null)return "空信息";



        if (StringUtils.isBlank(userDto.getPhone())
                || StringUtils.isEmpty(userDto.getPhone())
                ||!CommonUtils.isMobileNO(userDto.getPhone())
                ) return "手机号码有误";


        if (StringUtils.isBlank(userDto.getEmail())
                || StringUtils.isEmpty(userDto.getEmail())
                ||!CommonUtils.isEmail(userDto.getEmail())
                ) return "Email地址有误";


        if (StringUtils.isBlank(userDto.getUserName())
                || StringUtils.isEmpty(userDto.getUserName())
                ) return "用户名有误";


        if (userDto.getUserName().indexOf(",")!=-1
                || userDto.getUserName().indexOf(" ")!=-1
                ) return "用户名有空格或,号";


        String result = userService.checkUserExist(userDto);
        if(StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)
                ) return result;


        return null;
    }


    public static String getUser(IUserService userService,Integer userId,Integer currentCompanyId) throws Exception{
        Gson gson = CommonUtils.getGson();
        ResponseJson responseJson = new ResponseJson();

        if(userId != null && currentCompanyId != null && userId >0 && currentCompanyId > 0){
            Map<String, Object> paramMapTemp = new HashMap<>();
            paramMapTemp.put("companyId",currentCompanyId);
            paramMapTemp.put("userId",userId);
            Integer resultTemp = userService.usersPageCount(paramMapTemp);
            if(resultTemp == null || resultTemp < 1){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("当前公司下没该用户信息");
                return gson.toJson(responseJson);
            }
        }


        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity = userService.getUser(userEntity);

        if(userEntity == null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage("没有用户");
        }else{
            responseJson.setCode(ResponseCode.Code_200);
            userEntity.setLoginPwd(null);
            responseJson.setData(userEntity);
        }
        return gson.toJson(responseJson);

    }

    public static String updateUser(IUserService userService, UserDto userDto,Integer currentUserId)throws Exception{
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();

        Integer userDtoId = userDto.getId();

        if(userDtoId == null || userDtoId < 1){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage("用户id缺失");
        }


        String check = newOrUpdateUserCheck(userService,userDto);
        if(check != null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage(check);
            return gson.toJson(responseJson);
        }

        UserEntity userEntity = userService.updateUser(userDto,currentUserId);

        if(userEntity == null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage("更新失败");
        }else{

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");
        }

        return gson.toJson(responseJson);

    }

    public static String batchBan(IUserService userService,@RequestParam Map<String, String> params,Integer currentUserId,Integer currentCompanyId) throws Exception{
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
            userService.batchBan(list,type,currentUserId,currentCompanyId);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");
            return gson.toJson(responseJson);
        }
    }

    public static String usersPage(IUserService userService ,NativeWebRequest request,Integer currentCompanyId) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();
        Map<String,Object> paramMap = new HashMap<String, Object>();

        String status = request.getParameter("status");
        if(status !=null && StringUtils.isNotEmpty(status)) {
            paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
        }
        String departmentId = request.getParameter("departmentId");
        if(departmentId !=null && StringUtils.isNotEmpty(departmentId)) {
            paramMap.put("departmentId", Integer.valueOf(NumberUtils.toInt(departmentId, 0)));
        }
        if(currentCompanyId != null && currentCompanyId > 0){
            paramMap.put("companyId", currentCompanyId);
        }else{
            String companyId = request.getParameter("companyId");
            if(companyId !=null && StringUtils.isNotEmpty(companyId)) {
                paramMap.put("companyId", Integer.valueOf(NumberUtils.toInt(companyId, 0)));
            }
        }


        String phone = request.getParameter("phone");
        if(phone !=null && StringUtils.isNotEmpty(phone)) {
            paramMap.put("phone", phone);
        }
        String email = request.getParameter("email");
        if(email !=null && StringUtils.isNotEmpty(email)) {
            paramMap.put("email", email);
        }
        String userName = request.getParameter("name");
        if(userName !=null && StringUtils.isNotEmpty(userName)) {
            paramMap.put("userName", userName);
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
        List<UserDto> items = userService.usersPage(paramMap);
        Integer total = userService.usersPageCount(paramMap);
        data.put("items",items);
        data.put("total",total==null?0:total);
        responseJson.setData(data);
        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("搜索成功");

        return gson.toJson(responseJson);
    }
    public static String newUser(IUserService userService , IAdminUserService adminUserService, UserDto userDto, Integer currentUserId, Integer departmentId, Boolean isNewAdminUser) throws Exception {
        ResponseJson responseJson = new ResponseJson();
        Gson gson = CommonUtils.getGson();
        String check = OperationUserUtils.newOrUpdateUserCheck(userService,userDto);
        if(check != null){
            responseJson.setCode(ResponseCode.Code_Bussiness_Error);
            responseJson.setMessage(check);
            return gson.toJson(responseJson);
        }

        if(isNewAdminUser){
            adminUserService.newAdminUser(userDto,currentUserId);
        }else{
            userService.newUser(userDto,currentUserId,departmentId);
        }


        responseJson.setCode(ResponseCode.Code_200);
        responseJson.setMessage("新增成功");

        return gson.toJson(responseJson);
    }


}
