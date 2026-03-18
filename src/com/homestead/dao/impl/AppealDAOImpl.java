package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.Appeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 申诉DAO实现类，操作appeals表
 */

public class AppealDAOImpl extends BaseDAO {

    /**
     * 新增申诉记录
     * @param appeal
     * @return
     */
    public int addAppeal(Appeal appeal) {
        String sql = "insert into appeals(appeal_id,appeal_reason,appeal_time) values(?,?,?)";
        return update(sql,
                appeal.getAppId(),
                appeal.getAppealReason(),
                appeal.getAppealTime());
    }

    /**
     * 处理申诉 更新审核结果和意见
     * @param appealId
     * @param reviewResult
     * @param reviewReason
     * @return
     */
    public int handleAppeal(Integer appealId,String reviewResult,String reviewReason){
        String sql = "update appeals set review_result = ?,review_reason = ? where appeal_id = ?";
        return update(sql,reviewResult,reviewReason,appealId);
    }

    /**
     * 查询所有待处理申诉（review_result为空）
     * @return
     */
    public List<Appeal> getPendingAppeals(){
    	String sql = "select * from appeals where review_result IS NULL";
    	return query(sql, new ResultSetHandler<List<Appeal>>() {
			@Override
			public List<Appeal> handle(ResultSet rs) throws SQLException {
				List<Appeal> list = new ArrayList<>();
				while(rs.next()){
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

    public Appeal getAppealByAppId(Integer appId){
    	String sql = "select * from appeals where app_id = ?";
    	return query(sql, new ResultSetHandler<Appeal>() {
			@Override
			public Appeal handle(ResultSet rs) throws SQLException {
				if(rs.next()){
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
