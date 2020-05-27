package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.UserDto;
import com.zsw.services.ICompanyService;
import com.zsw.services.IUserService;
import com.zsw.utils.AdminUserAndUserUtils;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.ResponseCode;
import com.zsw.utils.UserStaticURLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/26.
 */
@RestController
@RequestMapping(UserStaticURLUtil.adminOperationUserController)
public class AdminOperationDepartmentController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminOperationDepartmentController.class);


    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
