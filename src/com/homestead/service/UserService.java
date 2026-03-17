package com.homestead.service;

import com.homestead.entity.User;

/**
 * 用户服务接口,封装用户相关业务逻辑，登录，权限校验
 */
public interface UserService {

    /**
     * 用户登录校验
     * @param username 用户名
     * @param password 密码
     * @return 用户信息，登录成功返回用户信息，失败返回null
     */
    User login(String username, String password);

    /**
     * 根据用户ID获取用户信息，用于审批人信息展示
     * @param userId 用户ID
     * @return 用户信息，user对象
     */
    User getUserById(Integer userId);

    /**
     * 校验用户权限
     * @param userId 用户ID
     * @param role 角色，村级审批/乡镇审批
     * @return true:有权限，false:无权限
     */
    boolean checkUserRole(Integer userId, String role);
}
