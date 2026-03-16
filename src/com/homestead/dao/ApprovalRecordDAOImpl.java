package com.homestead.dao;

import com.homestead.entity.ApprovalRecord;

/**
 * 审批记录DAO实现类，实现审批记录相关数据库操作
 */
public class ApprovalRecordDAOImpl extends BaseDAO implements ApprovalRecordDAO{
    @Override
    public int addApprovalRecord(ApprovalRecord record) {
        String sql = "INSERT INTO ApprovalRecords(app_id,level,opinion,result,approve_time)" +
                "VALUES(?,?,?,?,?,NOW())";
        return update(sql,
                record.getAppId(),
                record.getApproverId(),
                record.getLevel(),
                record.getOpinion(),
                record.getResult());
    }
}
