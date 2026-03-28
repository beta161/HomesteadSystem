package com.homestead.dao.impl;

import com.homestead.dao.ApplicationDAO;
import com.homestead.dao.BaseDAO;
import com.homestead.entity.Application;
import com.homestead.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationDAOImpl extends BaseDAO implements ApplicationDAO {

    @Override
    public int addApplication(Application application) {
        String sql = "INSERT INTO Applications(user_id, plot_area, purpose, status, apply_time, attach_count, current_approval_level) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, application.getUserId());
            pstmt.setDouble(2, application.getPlotArea());
            pstmt.setString(3, application.getPurpose());
            pstmt.setString(4, application.getStatus());
            pstmt.setInt(5, application.getAttachCount());
            pstmt.setString(6, application.getCurrentApprovalLevel());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return 0;
            }
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行SQL插入失败：" + sql, e);
        } finally {
            DBUtil.getInstance().close(conn, pstmt, rs);
        }
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
        // 如果 level 为 null 或空字符串，则设置 SQL 为 NULL
        String sql = "UPDATE Applications SET status = ?, current_approval_level = ? WHERE app_id = ?";
        // 使用 PreparedStatement 时，level 为 null 会自动设置 SQL NULL
        return update(sql, status, level, appId);
    }

    @Override
    public List<Application> findAll() {
        String sql = "SELECT app_id AS appId, user_id AS userId, plot_area AS plotArea, purpose, status, " +
                "apply_time AS applyTime, attach_count AS attachCount, current_approval_level AS currentApprovalLevel " +
                "FROM Applications";
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
        });
    }

    @Override
    public List<Application> findByUserId(Integer userId) {
        String sql = "SELECT app_id AS appId, user_id AS userId, plot_area AS plotArea, purpose, status, " +
                "apply_time AS applyTime, attach_count AS attachCount, current_approval_level AS currentApprovalLevel " +
                "FROM Applications WHERE user_id = ?";
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
        }, userId);
    }
}