package com.homestead.entity;

import java.util.Date;

public class User {
    private Integer userId;//用户id（主）
    private String username;//用户名
    private String password;//密码
    private String role;//角色：管理员/申请人/村级审批/乡镇审批
    private String phone;//电话
    private Date createTime;//创建时间

    public User() {
    }

    public User(Integer user_id, String username, String password, String role, String phone, Date create_time) {
        this.userId = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.createTime = create_time;
    }


    //常用构造方法 登录/新增用户

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getUser_id() {
        return userId;
    }

    public void setUser_id(Integer user_id) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreate_time() {
        return createTime;
    }

    public void setCreate_time(Date create_time) {
        this.createTime = createTime;
    }

    //tostring

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
