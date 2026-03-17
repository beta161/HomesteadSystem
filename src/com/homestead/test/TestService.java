package com.homestead.test;

import com.homestead.entity.Application;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.service.impl.UserServiceImpl;

public class TestService {
    public static void main(String[] args) {
        // 1. 测试用户登录
        UserServiceImpl userService = new UserServiceImpl();
        userService.login("user01", "123456");

        // 2. 测试提交申请
        ApplicationServiceImpl appService = new ApplicationServiceImpl();
        Application app = new Application();
        app.setUserId(1); // 申请人ID=1
        app.setPlotArea(120.5); // 面积120.5㎡
        app.setPurpose("建房"); // 申请用途
        appService.submitApplication(app);
    }
}