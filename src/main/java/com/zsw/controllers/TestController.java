package com.zsw.controllers;

import com.zsw.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangshaowei on 2020/4/2.
 */
@Controller
@RequestMapping("/declare_test")
public class TestController {
    @Autowired
    TestService testService;

    @RequestMapping("/isUser")
    @ResponseBody
    public String isUser(String userName, String passWord) {
        return testService.isUser(userName,passWord)==null?"no":"yes";
    }
}
