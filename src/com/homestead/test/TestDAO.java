package com.homestead.test;

import com.homestead.dao.UserDAOImpl;
import com.homestead.entity.User;

public class TestDAO {
    public static void main(String[] args) {
        // 测试用户登录
        UserDAOImpl userDAO = new UserDAOImpl();
        User user = userDAO.login("user01", "123456");
        if (user != null) {
            System.out.println("登录成功：" + user);
        } else {
            System.out.println("登录失败");
        }
    }
}
