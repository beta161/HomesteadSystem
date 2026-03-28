package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.dao.UserDAO;
import com.homestead.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户DAO实现类,实现用户相关数据库操作
 */
public class UserDAOImpl extends BaseDAO implements UserDAO {
    @Override
    public User login(String username, String password) {
        String sql = "SELECT user_id AS userId, username, password, role, phone, create_time AS createTime " +
                "FROM Users WHERE username = ? AND password = ?";
        return query(sql, new ResultSetHandler<User>() {
            @Override
            public User handle(ResultSet rs) throws SQLException {
                if (rs.next()){
                    User user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                }else{
                    return null;
                }
            }
        },username,password);
    }

    @Override
    public User findById(Integer userId) {
       String sql = "SELECT user_id = userID,username,password,role,phone,create_time AS createTime" +
               "FROM Users WHERE user_id = ?";
       return query(sql, new ResultSetHandler<User>() {
           @Override
           public User handle(ResultSet rs) throws SQLException {
               if (rs.next()){
                   User user = new User();
                   user.setUserId(rs.getInt("userID"));
                   user.setUsername(rs.getString("username"));
                   user.setPassword(rs.getString("password"));
                   user.setRole(rs.getString("role"));
                   user.setPhone(rs.getString("phone"));
                   user.setCreateTime(rs.getTimestamp("createTime"));
                   return user;
               }
               return null;
           }
       },userId);
    }

    public User findByUsername(String username) {
        String sql = "SELECT user_id AS userId, username, password, role, phone, create_time AS createTime FROM Users WHERE username = ?";
        return query(sql, rs -> {
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setPhone(rs.getString("phone"));
                user.setCreateTime(rs.getTimestamp("createTime"));
                return user;
            }
            return null;
        }, username);
    }

    @Override
    public int addUser(User user) {
        String sql = "INSERT INTO Users(username, password, role, phone, create_time) VALUES (?, ?, ?, ?, NOW())";
        return update(sql, user.getUsername(), user.getPassword(), user.getRole(), user.getPhone());
    }
} 
