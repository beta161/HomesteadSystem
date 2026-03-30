package com.homestead.service;

import com.homestead.entity.Application;

import java.util.List;


/**
 * 申请service接口，封装宅基地申请相关业务逻辑
 */
public interface ApplicationService {

    /**
     * 提交宅基地申请
     * @param application 申请信息，包含申请人ID，面积，用途等
     * @return true:提交成功，false:提交失败
     */
    boolean submitApplication(Application  application);

    /**
     * 根据状态查询申请信息
     * @param status 申请状态
     * @return 申请信息列表
     */
    List<Application> getApplicationsByStatus(String status);

    /**
     * 根据当前审批级别查询申请信息,用于代办提醒
     * @param level 审批环节，乡镇/村级
     * @return 申请信息列表
     */
    List<Application> getApplicationsByCurrentLevel(String level);

    /**
     * 根据申请ID查询申请信息
     * @param appId 申请ID
     * @return 申请对象
     */
    Application getApplicationById(Integer appId);

    /**
     * 更新申请状态和当前审批环节（审批通过/驳回时调用）
     * @param appId 申请ID
     * @param status 申请状态（初审通过/待乡镇复审）
     * @param level 审批级别（乡镇/空）
     * @return true:更新成功，false:更新失败
     */
    boolean updateAppStatusAndLevel(Integer appId, String status, String level);


    List<Application> getAllApplications();

    List<Application> getApplicationsByUserId(Integer userId);

    List<Object[]> getMonthlyStatistics(int limit);

    List<Application> getApplicationsByCurrentLevelWithPage(String level, String keyword, int pageNum, int pageSize);
    int countApplicationsByCurrentLevel(String level, String keyword);


}
