package com.homestead.dao;


import com.homestead.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//通用DAO父类，封装JDBC增删改查通用操作
public class BaseDAO {
    //通用更新方法增/删/改
    public int update(String sql,Object... args){
        Connection conn = null;//接口
        PreparedStatement pstmt = null;
        try {
            //获取数据库连接
            conn = DBUtil.getInstance().getConnection();
            //预编译sql
            pstmt = conn.prepareStatement(sql);
            //填充参数
            for (int i = 0;i < args.length;i++){
                pstmt.setObject(i + 1,args[i]);//遍历可变参数，赋值
            }
            //执行更新并返回影响行数
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();//打印异常堆栈
            throw new RuntimeException("执行SQL更新失败："+ sql,e);
        }finally {
            //关闭资源
            DBUtil.getInstance().close(conn,pstmt);
        }
    }
    //  定义内部泛型接口 ResultSetHandler
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
    //通用查询方法，子类重写处理结果集
    public <T> T query(String sql, ResultSetHandler<T> handler,Object... args){//泛型接口
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            //填充参数
            for (int i = 0;i < args.length;i++){
                pstmt.setObject(i + 1,args[i]);
            }
            //执行查询，返回结果集
            rs = pstmt.executeQuery();//query是select专用，返回结果集，即所有数据
            //处理结果集，由子类实现
            return handler.handle(rs);
        } catch (SQLException e) {
           e.printStackTrace();
           return null;
        }finally {
            DBUtil.getInstance().close(conn,pstmt,rs);
        }
    }
}
