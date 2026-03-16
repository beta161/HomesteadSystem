package com.homestead.dao;

import com.homestead.entity.User;

//用户DAO接口，定义用户相关数据库操作
public interface UserDAO {
    /**
     * 用户登录校验
     * @param username
     * @param password
     * @return 登陆成功返回User对象，失败返回null
     */

    User login(String username,String password);

    /**
     * 根据用户id查询用户
     * @param userId
     * @return User对象
     */
    User findById(Integer userId);

}
