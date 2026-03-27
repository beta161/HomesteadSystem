package com.homestead.dao.impl;

import com.homestead.dao.ApprovalRecordDAO;
import com.homestead.dao.BaseDAO;
import com.homestead.entity.ApprovalRecord;

public class ApprovalRecordDAOImpl extends BaseDAO implements ApprovalRecordDAO {
    @Override
    public int addApprovalRecord(ApprovalRecord record) {
        String sql = "INSERT INTO ApprovalRecords(app_id, approver_id, level, opinion, result, approve_time) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
        return update(sql,
                record.getAppId(),
                record.getApproverId(),
                record.getLevel(),
                record.getOpinion(),
                record.getResult());
    }
}