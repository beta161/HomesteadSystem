package com.homestead.service;

import com.homestead.entity.ApprovalRecord;

/**
 * 审批记录service接口，封装审批记录相关业务逻辑
 */
public interface ApprovalRecordService {

    /**
     * 新增审批记录，审批完成后调用
     * @param record 审批记录对象，包含申请ID，审批人ID，审批结果，审批意见
     * @return 成功返回true，失败返回false
     */
    boolean addApprovalRecord(ApprovalRecord record);
}
