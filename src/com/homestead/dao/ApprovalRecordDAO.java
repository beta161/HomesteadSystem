package com.homestead.dao;

import com.homestead.entity.ApprovalRecord;

/**
 * 审批记录DAO接口，定义审批记录相关数据库操作
 */
public interface ApprovalRecordDAO {

    /**
     * 新增审批记录
     * @param record 审批记录对象
     * @return 新增成功返回1，失败0
     */
    int addApprovalRecord(ApprovalRecord record);
}
