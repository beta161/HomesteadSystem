package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.SystemLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统日志DAO实现类，操作system_logs表
 */
public class SystemLogDAOImpl extends BaseDAO {

    /**
     * 新增系统日志记录
     * @param log
     * @return
     */
    public int addSystemLog(SystemLog log){
        String sql = "INSERT INTO SystemLogs(user_id,operation,detail) VALUES (?,?,?)";
        return update(sql,log.getUserId(),log.getOperation(),log.getDetail());
    }

    /**
     * 按时间倒序查询所有系统日志
     * @return
     */
    public List<SystemLog> getAllLogsOrdersByTime(){
       String sql = "SELECT * FROM SystemLogs ORDER BY op_time DESC";
       return query(sql, new ResultSetHandler<List<SystemLog>>() {
           @Override
           public List<SystemLog> handle(ResultSet rs) throws SQLException {
               List<SystemLog> list = new ArrayList<>();
               while (rs.next()){
                   SystemLog log = new SystemLog();
                   log.setLogId(rs.getInt("log_id"));
                   log.setUserId(rs.getInt("user_id"));
                   log.setOperation(rs.getString("operation"));
                   log.setDetail(rs.getString("detail"));
                   log.setOpTime(rs.getTimestamp("op_time"));
                   list.add(log);
               }
               return list;
           }
       });
    }

    /**
     * 按用户id查询系统日志，普通用户用
     * @param userId
     * @return
     */
    public List<SystemLog> getLogsByUserId(Integer userId){
       String sql = "SELECT * FROM SystemLogs WHERE user_id = ? ORDER BY op_time DESC";
       return query(sql, new ResultSetHandler<List<SystemLog>>() {
           @Override
           public List<SystemLog> handle(ResultSet rs) throws SQLException {
               List<SystemLog> list = new ArrayList<>();
               while (rs.next()){
                   SystemLog log = new SystemLog();
                   log.setLogId(rs.getInt("log_id"));
                   log.setUserId(rs.getInt("user_id"));
                   log.setOperation(rs.getString("operation"));
                   log.setDetail(rs.getString("detail"));
                   log.setOpTime(rs.getTimestamp("op_time"));
               }
                return list;
           }
       },userId);
    }
}
