package com.homestead.service.impl;

import com.homestead.dao.impl.ApprovalRecordDAOImpl;
import com.homestead.entity.ApprovalRecord;
import com.homestead.service.ApprovalRecordService;

/**
 * 审批记录Service实现类，实现审批记录相关业务逻辑
 */
public class ApprovalRecordServiceImpl implements ApprovalRecordService {

    //依赖DAO层
    private ApprovalRecordDAOImpl approvalRecordDAO = new ApprovalRecordDAOImpl();
    @Override
    public boolean addApprovalRecord(ApprovalRecord record) {
        //校验
        if (record == null || record.getAppId() == null || record.getApproverId() == null || record.getLevel() == null || record.getOpinion() == null || record.getResult() == null){
            System.out.println("新增审批记录失败：申请ID/审批人ID/审批级别/结果不能为空");
            return false;
        }
        //补充默认值，意见为空时填无
        if (record.getOpinion() == null || record.getOpinion().isEmpty()){
            record.setOpinion("无");
        }
        //调用DAO层新增
        int result = approvalRecordDAO.addApprovalRecord(record);
        boolean success = result > 0;
        if ( success){
            System.out.println("新增审批记录成功：申请ID=" + record.getAppId() + ",审批级别 = " + record.getLevel() + ",结果 = " + record.getResult());

        }else {
            System.out.println("新增审批记录失败");
        }
        return success;

    }
}
