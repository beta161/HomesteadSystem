package com.homestead.dao;

import com.homestead.entity.ApprovalTimer;

import java.util.List;

/**
 * 审批时限类接口，定义审批时限类相关数据库操作
 */
public interface ApprovalTimerDAO {

    /**
     * 新增审批时限记录
     * @param timer 审批时限对象
     * @return 成1，败0
     */
    int addApprovalTimer(ApprovalTimer timer);

    /**
     * 根据申请ID和审批级别查询时限记录
     * @param appId
     * @param level
     * @return 审批时限对象
     */
    ApprovalTimer findByAppIdAndLevel(Integer appId,String level);

    /**
     * 更新超时状态
     * @param timerId 时限记录ID
     * @param isOverdue 是否超时0/1
     * @return 成1，败0
     */
    int updateOverdueStatus(Integer timerId,Integer isOverdue);

    /**
     * 更新提醒状态
     * @param timerId
     * @param remindStatus 状态 未/已
     * @return 成1，败0
     */
    int updateRemindStatus(Integer timerId,String remindStatus);

    /**
     * 查询指定审批级别下的超时记录
     * @param level 村级/乡镇
     * @return 超时记录列表
     */
    List<ApprovalTimer> findOverdueByLevel(String level);

    /**
     * 查询指定级别下的代办记录
     * @param level
     * @return 代办记录列表
     */
    List<ApprovalTimer> findTodoByLevel(String level);
}
