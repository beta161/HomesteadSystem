package com.homestead.service.impl;

import com.homestead.dao.impl.ApplicationDAOImpl;
import com.homestead.entity.Application;
import com.homestead.service.ApplicationService;
import com.homestead.service.ApprovalTimerService;

import java.util.Collections;
import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {
    private ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
    private ApprovalTimerService timerService = new ApprovalTimerServiceImpl(); // 新增时限服务

    @Override
    public boolean submitApplication(Application application) {
        if (application == null || application.getUserId() == null || application.getPlotArea() == null || application.getPurpose() == null) {
            System.out.println("提交申请失败：申请人ID/面积/用途不能为空");
            return false;
        }
        if (application.getStatus() == null || application.getStatus().isEmpty()) {
            application.setStatus("待村级审批");
        }
        if (application.getAttachCount() == null) {
            application.setAttachCount(0);
        }
        if (application.getCurrentApprovalLevel() == null || application.getCurrentApprovalLevel().isEmpty()) {
            application.setCurrentApprovalLevel("村级");
        }

        int generatedId = applicationDAO.addApplication(application);
        boolean success = generatedId > 0;
        if (success) {
            application.setAppId(generatedId);
            System.out.println("提交申请成功:申请ID = " + generatedId + ",申请人ID = " + application.getUserId());

            // 新增：自动插入村级审批时限记录（7天）
            boolean timerInit = timerService.initApprovalTimer(generatedId, "村级", 7);
            if (timerInit) {
                System.out.println("村级审批时限初始化成功，申请ID = " + generatedId);
            } else {
                System.err.println("村级审批时限初始化失败，申请ID = " + generatedId);
            }
        } else {
            System.out.println("提交申请失败");
        }
        return success;
    }

    // 其余方法保持不变
    @Override
    public List<Application> getApplicationsByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }
        return applicationDAO.findByStatus(status);
    }

    @Override
    public List<Application> getApplicationsByCurrentLevel(String level) {
        if (level == null || level.isEmpty()) {
            return null;
        }
        return applicationDAO.findByCurrentLevel(level);
    }

    @Override
    public Application getApplicationById(Integer appId) {
        if (appId == null || appId <= 0) {
            return null;
        }
        return applicationDAO.findById(appId);
    }

    @Override
    public boolean updateAppStatusAndLevel(Integer appId, String status, String level) {
        if (appId == null || appId <= 0 || status == null || status.isEmpty()) {
            System.out.println("更新申请状态失败：申请ID/状态不能为空");
            return false;
        }
        int result = applicationDAO.updateStatusAndLevel(appId, status, level);
        boolean success = result > 0;
        if (success) {
            System.out.println("更新申请状态成功:申请ID = " + appId + ",新状态 = " + status + ",新环节 = " + level);
        } else {
            System.out.println("更新申请状态失败：申请ID = " + appId);
        }
        return success;
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationDAO.findAll();
    }
}