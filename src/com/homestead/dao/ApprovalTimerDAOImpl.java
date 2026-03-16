package com.homestead.dao;

import com.homestead.entity.ApprovalRecord;
import com.homestead.entity.ApprovalTimer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 审批时限DAO实现类，实现审批时限的相关数据库操作
 */
public class ApprovalTimerDAOImpl extends BaseDAO implements ApprovalTimerDAO{

    @Override
    public int addApprovalTimer(ApprovalTimer timer) {
       String sql = "INSERT INTO ApprovalTimers(app_id,approval_level,start_time,deadline,is_overdue,remind_status,create_time)" +
               "VALUES (?,?,?,?,?,?,NOW())";
       return update(sql,
               timer.getAppId(),
               timer.getApprovalLevel(),
               timer.getStartTime(),
               timer.getDeadline(),
               timer.getIsOverdue(),
               timer.getRemindStatus());
    }

    @Override
    public ApprovalTimer findByAppIdAndLevel(Integer appId, String level) {
       String sql = "SELECT timer_id AS timerID,app_id AS appId,approval_level AS approvalLevel," +
               "start_time AS startTime,deadline,is_overdue AS isOverdue,remind_status AS remindStatus,create_time AS createTime" +
               "FROM ApprovalTimers WHERE app_id = ? AND approval_level = ?";
       return query(sql, new ResultSetHandler<ApprovalTimer>() {
           @Override
           public ApprovalTimer handle(ResultSet rs) throws SQLException {
               if (rs.next()){
                   ApprovalTimer timer = new ApprovalTimer();
                   timer.setTimeId(rs.getInt("timerID"));
                   timer.setAppId(rs.getInt("appId"));
                   timer.setApprovalLevel(rs.getString("approvalLevel"));
                   timer.setStartTime(rs.getDate("startTime"));
                   timer.setDeadline(rs.getDate("deadline"));
                   timer.setIsOverdue(rs.getInt("isOverdue"));
                   timer.setRemindStatus(rs.getString("remindStatus"));
                   timer.setCreateTime(rs.getDate("createTime"));
                   return timer;
               }
               return null;
           }
       },appId,level);
    }

    @Override
    public int updateOverdueStatus(Integer timerId, Integer isOverdue) {
       String sql = "UPDATE ApprovalTimers SET is_overdue = ? WHERE timer_id = ?";
       return update(sql,isOverdue,timerId);
    }

    @Override
    public int updateRemindStatus(Integer timerId, String remindStatus) {
        String sql = "UPDATE ApprovalTimers SET remind_status = ? WHERE timer_id = ?";
        return update(sql,remindStatus,timerId);
    }

    @Override
    public List<ApprovalTimer> findOverdueByLevel(String level) {
       String sql = "SELECT timer_id AS timerId,app_id AS appId,approval_level AS approvalLevel," +
               "start_time AS startTime,deadline,is_overdue AS isOverdue,remind_status AS remindStatus,create_time AS createTime" +
               "FROM ApprovalTimers WHERE approval_level = ? AND is_overdue = 1";
       return query(sql, rs -> {
           List<ApprovalTimer> list = new ArrayList<>();
           while (rs.next()){
               ApprovalTimer timer = new ApprovalTimer();
               timer.setTimeId(rs.getInt("timerId"));
               timer.setAppId(rs.getInt("appId"));
               timer.setApprovalLevel(rs.getString("approvalLevel"));
               timer.setStartTime(rs.getDate("startTime"));
               timer.setDeadline(rs.getDate("deadline"));
               timer.setIsOverdue(rs.getInt("isOverdue"));
               timer.setRemindStatus(rs.getString("remindStatus"));
               timer.setCreateTime(rs.getDate("createTime"));
               list.add(timer);
           }
           return list;
       }, level);
    }

    @Override
    public List<ApprovalTimer> findTodoByLevel(String level) {
        String sql = "SELECT timer_id AS timerId,app_id AS appId,approval_level AS approvalLevel," +
                "start_time AS startTime,deadline,is_overdue AS isOverdue,remind_status AS remindStatus,create_time AS createTime" +
                "FROM ApprovalTimers WHERE approval_level = ? AND remind_status = 0";
        return query(sql, new ResultSetHandler<List<ApprovalTimer>>() {
            @Override
            public List<ApprovalTimer> handle(ResultSet rs) throws SQLException {
                 List<ApprovalTimer> list = new ArrayList<>();
                while (rs.next()){
                    ApprovalTimer timer = new ApprovalTimer();
                    timer.setTimeId(rs.getInt("timerId"));
                    timer.setAppId(rs.getInt("appId"));
                    timer.setApprovalLevel(rs.getString("approvalLevel"));
                    timer.setStartTime(rs.getDate("startTime"));
                    timer.setDeadline(rs.getDate("deadline"));
                    timer.setIsOverdue(rs.getInt("isOverdue"));
                    timer.setRemindStatus(rs.getString("remindStatus"));
                    timer.setCreateTime(rs.getDate("createTime"));
                    list.add(timer);
                }
                return list;
            }
        },level);
    }
}
