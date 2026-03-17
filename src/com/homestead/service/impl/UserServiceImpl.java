package com.homestead.service.impl;

import com.homestead.dao.impl.UserDAOImpl;
import com.homestead.entity.User;
import com.homestead.service.UserService;

/**
 * 用户service实现类，实现用户相关业务逻辑
 */
public class UserServiceImpl implements UserService {
    //依赖DAO层，创建UserDAOImpl对象
    private UserDAOImpl userDAO = new UserDAOImpl();
    @Override
    public User login(String username, String password) {
        //空值判断，避免sql异常
        if (username == null || username.isEmpty() || password == null || password.isEmpty()){
            System.out.println("登录失败：用户名/密码不能为空");
            return null;
        }
        //调用DAO层查询
        User user = userDAO.login(username, password);
        //逻辑校验
        if (user == null){
            System.out.println("登录失败：用户名/密码错误");
        }else {
            System.out.println("登录成功：用户ID = " + user.getUserId() + ",角色 = " + user.getRole());
        }
        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        //校验
        if (userId == null || userId <= 0){
            return null;
        }
        //调用DAO层查询
        return userDAO.findById(userId);
    }

    @Override
    public boolean checkUserRole(Integer userId, String role) {
        //校验
        if (userId == null  || role == null || role.isEmpty()){
            return false;
        }
        //查询用户
        User user = getUserById(userId);
        //校验角色
        return user != null && role.equals(user.getRole());
    }
}
