package com.homestead.service.impl;

import com.homestead.dao.impl.ApplicationDAOImpl;
import com.homestead.entity.Application;
import com.homestead.service.ApplicationService;

import java.util.Collections;
import java.util.List;


/**
 * 申请Service实现类，实现申请相关业务逻辑
 */
public class ApplicationServiceImpl implements ApplicationService {
    //依赖DAO层
    private ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
    @Override
    public boolean submitApplication(Application application) {
        //校验，核心字段非空
        if (application == null || application.getUserId() == null || application.getPlotArea() == null || application.getPurpose() == null){
            System.out.println("提交申请失败：申请人ID/面积/用途不能为空");
            return false;
        }
        //补充默认值，状态，附件数，当前审批环节
        if (application.getStatus() == null || application.getStatus().isEmpty()){
            application.setStatus("待村级初审"); //默认待村级初审
        }
        if (application.getAttachCount() == null){
            application.setAttachCount(0); //默认无附件
        }
        if (application.getCurrentApprovalLevel() == null || application.getCurrentApprovalLevel().isEmpty()){
            application.setCurrentApprovalLevel("村级"); //默认当前审批环节为村级
        }
        //调用DAO层新增
        int result = applicationDAO.addApplication(application);
        boolean success = result > 0;
        if (success){
            System.out.println("提交申请成功:申请ID = " + application.getAppId() + ",申请人ID = " + application.getUserId());

        }else{
            System.out.println("提交申请失败");
        }
        return success;
    }

    @Override
    public List<Application> getApplicationsByStatus(String status) {

        //入参校验
        if (status == null || status.isEmpty()){
            return null;
        }
        //调用DAO层
        return applicationDAO.findByStatus(status);
    }

    @Override
    public List<Application> getApplicationsByCurrentLevel(String level) {
        if (level == null || level.isEmpty()){
            return null;
        }
        return applicationDAO.findByCurrentLevel(level);
    }

    @Override
    public Application getApplicationById(Integer appId) {
        if (appId == null || appId <= 0){
            return null;
        }
        return applicationDAO.findById(appId);
    }

    @Override
    public boolean updateAppStatusAndLevel(Integer appId, String status, String level) {
        if (appId == null || appId <= 0 || status == null || status.isEmpty() || level == null || level.isEmpty()){
            System.out.println("更新申请状态失败：申请ID/状态不能为空");
            return false;
        }
        int result = applicationDAO.updateStatusAndLevel(appId, status, level);
        boolean success = result > 0;
        if (success){
            System.out.println("更新申请状态成功:申请ID = " + appId + ",新状态 = " + status + ",新环节 = " + level);
        }else{
            System.out.println("更新申请状态失败：申请ID = " + appId);
        }
        return success;
    }
}
