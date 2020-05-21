package com.zsw.controllers;

import com.zsw.controller.BaseController;
import com.zsw.utils.UserStaticURLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshaowei on 2020/4/26.
 */
@RestController
@RequestMapping(UserStaticURLUtil.adminUserController)
public class AdminUserController extends BaseController {
}
