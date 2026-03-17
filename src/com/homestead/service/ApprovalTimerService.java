package com.homestead.service;

import com.homestead.entity.ApprovalTimer;

import java.util.List;

/**
 * 审批时限service接口，封装审批时限，提醒相关业务逻辑
 */
public interface ApprovalTimerService {
    /**
     * 初始化审批时限 提交申请/进入新审批环节时调用
     * @param appId 申请ID
     * @param level 审批环节（村级/乡镇）
     * @param days 审批时限天数，如村级3天，乡镇5天
     * @return 成功：true，失败：false
     */
    boolean initApprovalTimer(Integer appId,String level,int days);

    /**
     * 查询指定申请+环节的时限记录
     * @param appId 申请ID
     * @param level 审批环节（村级/乡镇）
     * @return 审批时限对象
     */
    ApprovalTimer getTimerByAppIdAndLevel(Integer appId,String level);

    /**
     * 检查并更新超时状态，定时任务/待办查询时调用
     * @return  超时记录数量
     */
    int checkAndUpdateOverdueStatus();

    /**
     * 更新提醒状态,发送提醒后调用
     * @param timerId 时限ID
     * @param remindStatus 提醒状态，已提醒
     * @return  成功：true，失败：false
     */
    boolean updateRemindStatus(Integer timerId,String remindStatus);

    /**
     * 查询指定环节的超时记录，用于超时提醒
     * @param level 审批环节（村级/乡镇）
     * @return  超时记录列表
     */
    List<ApprovalTimer> getOverdueTimersByLevel(String level);

    /**
     * 查询指定环节的待办记录，用于待办查询(未超时）
     * @param level 审批环节（村级/乡镇）
     * @return  待办记录列表
     */
    List<ApprovalTimer> getTodoTimersByLevel(String level);
}
