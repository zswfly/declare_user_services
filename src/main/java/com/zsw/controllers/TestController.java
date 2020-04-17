package com.zsw.controllers;

import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangshaowei on 2020/4/17.
 */

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @RequestMapping(value="/test", method= RequestMethod.POST)
    @ResponseBody
    @Permission(code="user.test.test",name = "测试",description = "test")
    public void test() throws Exception {

    }
}
