package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import com.zsw.services.IUserService;
import com.zsw.utils.CommonStaticWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangshaowei on 2020/4/17.
 */

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    IUserService userService;

    @RequestMapping(value="/test", method= RequestMethod.POST)
    @ResponseBody
    @Permission(code="user.test.test",name = "测试",description = "test")
    public void test(HttpServletRequest request) throws Exception {

        System.out.print(1);
    }

    @RequestMapping(value= CommonStaticWord.System_Url+"/test2", method= RequestMethod.POST)
    @ResponseBody
    public String test2(List<Integer> ids) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(this.userService.getUsersByIds(ids));
    }
}
