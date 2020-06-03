package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.annotations.Permission;
import com.zsw.controller.BaseController;
import com.zsw.services.IUserService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangshaowei on 2020/4/17.
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    IUserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value="/test", method= RequestMethod.POST)
    //@Permission(code="user.test.test",name = "测试",description = "test")
    public void test(HttpServletRequest request) throws Exception {
        LOG.trace("这是 test info 级别");
        LOG.debug("这是 test debug 级别");
        LOG.info("这是 test info 级别");
        LOG.warn("这是 test warn 级别");
        LOG.error("这是 test error 级别");
        int i = 1/0;
        System.out.print(1);
    }

    @RequestMapping(value= CommonStaticWord.System_Url+"/test2", method= RequestMethod.POST)
    public String test2(List<Integer> ids) throws Exception {
        Gson gson = CommonUtils.getGson();
        return gson.toJson(this.userService.getUsersByIds(ids));
    }

    @Override
    public Logger getLOG(){
        return this.LOG;
    }
}
