package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.Appeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppealDAOImpl extends BaseDAO {

    /**
     * 新增申诉记录
     */
    public int addAppeal(Appeal appeal) {
        // 修正：列名应为 app_id, appeal_reason, appeal_time，appeal_id 为自增主键
        String sql = "INSERT INTO appeals(app_id, appeal_reason, appeal_time) VALUES (?, ?, ?)";
        return update(sql,
                appeal.getAppId(),
                appeal.getAppealReason(),
                appeal.getAppealTime());
    }

    /**
     * 处理申诉
     */
    public int handleAppeal(Integer appealId, String reviewResult, String reviewReason) {
        String sql = "UPDATE appeals SET review_result = ?, review_opinion = ? WHERE appeal_id = ?";
        return update(sql, reviewResult, reviewReason, appealId);
    }

    /**
     * 查询所有待处理申诉
     */
    public List<Appeal> getPendingAppeals() {
        String sql = "SELECT * FROM appeals WHERE review_result IS NULL";
        return query(sql, new ResultSetHandler<List<Appeal>>() {
            @Override
            public List<Appeal> handle(ResultSet rs) throws SQLException {
                List<Appeal> list = new ArrayList<>();
                while (rs.next()) {
                    Appeal appeal = new Appeal();
                    appeal.setAppealId(rs.getInt("appeal_id"));
                    appeal.setAppId(rs.getInt("app_id"));
                    appeal.setAppealReason(rs.getString("appeal_reason"));
                    appeal.setAppealTime(rs.getTimestamp("appeal_time"));
                    appeal.setReviewResult(rs.getString("review_result"));
                    appeal.setReviewOpinion(rs.getString("review_opinion"));
                    list.add(appeal);
                }
                return list;
            }
        });
    }

    public Appeal getAppealByAppId(Integer appId) {
        String sql = "SELECT * FROM appeals WHERE app_id = ?";
        return query(sql, new ResultSetHandler<Appeal>() {
            @Override
            public Appeal handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    Appeal appeal = new Appeal();
                    appeal.setAppealId(rs.getInt("appeal_id"));
                    appeal.setAppId(rs.getInt("app_id"));
                    appeal.setAppealReason(rs.getString("appeal_reason"));
                    appeal.setAppealTime(rs.getTimestamp("appeal_time"));
                    appeal.setReviewResult(rs.getString("review_result"));
                    appeal.setReviewOpinion(rs.getString("review_opinion"));
                    return appeal;
                }
                return null;
            }
        }, appId);
    }
}