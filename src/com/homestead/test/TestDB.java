package com.homestead.test;

import com.homestead.util.DBUtil;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DBUtil.getInstance().getConnection();//抽象类不能创建对象
        if (conn != null){
            System.out.println("数据库连接成功！");
        }else {
            System.out.println("数据库连接失败！");
        }
    }
}
