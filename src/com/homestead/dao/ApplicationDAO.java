package com.homestead.dao;

import com.homestead.entity.Application;
import com.mysql.cj.x.protobuf.MysqlxSession;

import java.util.List;

/*
申请DAO接口，定义申请相关数据操作
 */
public interface ApplicationDAO {

    /**
     *新增申请
     * @param application 申请对象
     * @return 新增成功返回1，失败返回0
     */
    int addApplication(Application application);

    /**
     *根据状态查询申请列表
     * @param status 申请状态：如待村级审批
     * @return 申请列表
     */
    List<Application> findByStatus(String status);

    /**
     * 根据当前审批环节查询申请列表
     * @param level 审批环节村级/乡镇
     * @return 申请列表
     */
    List<Application> findByCurrentLevel(String level);

    /**
     * 根据申请ID查询申请
     * @param appId 申请ID
     * @return 申请对象
     */
    Application findById(Integer appId);

    /**
     * 更新申请状态和当前审批环节
     * @param appId 申请id
     * @param status 申请状态
     * @param level 审批环节
     * @return 更新成功返回1，失败0
     */
    int updateStatusAndLevel(Integer appId,String status,String level);
    /**
     * 查询所有申请
     */
    List<Application> findAll();

    List<Application> findByUserId(Integer userId);

    List<Object[]> getMonthlyStatistics(int limit);
}
