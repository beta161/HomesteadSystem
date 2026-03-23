package com.homestead.dao.impl;

import com.homestead.dao.ApplicationDAO;
import com.homestead.dao.BaseDAO;
import com.homestead.entity.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAOImpl extends BaseDAO implements ApplicationDAO {

    @Override
    public int addApplication(Application application) {
        String sql = "INSERT INTO Applications(user_id, plot_area, purpose, status, apply_time, attach_count, current_approval_level) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?)";
        return update(sql,
                application.getUserId(),
                application.getPlotArea(),
                application.getPurpose(),
                application.getStatus(),
                application.getAttachCount(),
                application.getCurrentApprovalLevel());
    }

    @Override
    public List<Application> findByStatus(String status) {
        String sql = "SELECT app_id AS appId, user_id AS userId, plot_area AS plotArea, purpose, status, " +
                "apply_time AS applyTime, attach_count AS attachCount, current_approval_level AS currentApprovalLevel " +
                "FROM Applications WHERE status = ?";
        return query(sql, new ResultSetHandler<List<Application>>() {
            @Override
            public List<Application> handle(ResultSet rs) throws SQLException {
                List<Application> list = new ArrayList<>();
                while (rs.next()) {
                    Application app = new Application();
                    app.setAppId(rs.getInt("appId"));
                    app.setUserId(rs.getInt("userId"));
                    app.setPlotArea(rs.getDouble("plotArea"));
                    app.setPurpose(rs.getString("purpose"));
                    app.setStatus(rs.getString("status"));
                    app.setApplyTime(rs.getTimestamp("applyTime"));
                    app.setAttachCount(rs.getInt("attachCount"));
                    app.setCurrentApprovalLevel(rs.getString("currentApprovalLevel"));
                    list.add(app);
                }
                return list;
            }
        }, status);
    }

    @Override
    public List<Application> findByCurrentLevel(String level) {
        String sql = "SELECT app_id AS appId, user_id AS userId, plot_area AS plotArea, purpose, status, " +
                "apply_time AS applyTime, attach_count AS attachCount, current_approval_level AS currentApprovalLevel " +
                "FROM Applications WHERE current_approval_level = ?";
        return query(sql, new ResultSetHandler<List<Application>>() {
            @Override
            public List<Application> handle(ResultSet rs) throws SQLException {
                List<Application> list = new ArrayList<>();
                while (rs.next()) {
                    Application app = new Application();
                    app.setAppId(rs.getInt("appId"));
                    app.setUserId(rs.getInt("userId"));
                    app.setPlotArea(rs.getDouble("plotArea"));
                    app.setPurpose(rs.getString("purpose"));
                    app.setStatus(rs.getString("status"));
                    app.setApplyTime(rs.getTimestamp("applyTime"));
                    app.setAttachCount(rs.getInt("attachCount"));
                    app.setCurrentApprovalLevel(rs.getString("currentApprovalLevel"));
                    list.add(app);
                }
                return list;
            }
        }, level);
    }

    @Override
    public Application findById(Integer appId) {
        String sql = "SELECT app_id AS appId, user_id AS userId, plot_area AS plotArea, purpose, status, " +
                "apply_time AS applyTime, attach_count AS attachCount, current_approval_level AS currentApprovalLevel " +
                "FROM Applications WHERE app_id = ?";
        return query(sql, new ResultSetHandler<Application>() {
            @Override
            public Application handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    Application app = new Application();
                    app.setAppId(rs.getInt("appId"));
                    app.setUserId(rs.getInt("userId"));
                    app.setPlotArea(rs.getDouble("plotArea"));
                    app.setPurpose(rs.getString("purpose"));
                    app.setStatus(rs.getString("status"));
                    app.setApplyTime(rs.getTimestamp("applyTime"));
                    app.setAttachCount(rs.getInt("attachCount"));
                    app.setCurrentApprovalLevel(rs.getString("currentApprovalLevel"));
                    return app;
                }
                return null;
            }
        }, appId);
    }

    @Override
    public int updateStatusAndLevel(Integer appId, String status, String level) {
        String sql = "UPDATE Applications SET status = ?, current_approval_level = ? WHERE app_id = ?";
        return update(sql, status, level, appId);
    }
}