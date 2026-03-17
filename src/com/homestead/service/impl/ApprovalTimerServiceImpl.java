package com.homestead.service.impl;

import com.homestead.dao.impl.ApprovalTimerDAOImpl;
import com.homestead.entity.ApprovalTimer;
import com.homestead.service.ApprovalTimerService;

import javax.xml.bind.Element;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 审批时限service实现类，实现审批时限，提醒相关业务逻辑
 */
public class ApprovalTimerServiceImpl implements ApprovalTimerService {
    //依赖DAO层
    private ApprovalTimerDAOImpl approvalTimerDAO = new ApprovalTimerDAOImpl();
    @Override
    public boolean initApprovalTimer(Integer appId, String level, int days) {
        if (appId == null || appId <= 0 || level == null || level.isEmpty() || days <= 0){
            System.out.println("初始化审批时限失败：申请ID/环节/天数不合法");
            return false;
        }
        //计算开始时间（当前时间）和截止时间，当前+days
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_MONTH, days);//加days天
        Date deadline = calendar.getTime();
        //创建时限对象，默认未超时，未提醒
        ApprovalTimer timer = new ApprovalTimer();
        timer.setAppId(appId);
        timer.setApprovalLevel(level);
        timer.setStartTime(startTime);
        timer.setDeadline(deadline);
        timer.setIsOverdue(0);
        timer.setRemindStatus("未提醒");
        //调用DAO层新增
        int result = approvalTimerDAO.addApprovalTimer(timer);
        boolean success = result > 0;
        if ( success){
            System.out.println("初始化审批时限成功：申请ID = " + appId + ",环节 = " + level + ",截止时间 = " + deadline);

        }else {
            System.out.println("初始化审批时限失败");
        }
        return success;
    }

    @Override
    public ApprovalTimer getTimerByAppIdAndLevel(Integer appId, String level) {
        if (appId == null || appId <= 0 || level == null || level.isEmpty()){
            return null;
        }
        return approvalTimerDAO.findByAppIdAndLevel(appId, level);
    }

    @Override
    public int checkAndUpdateOverdueStatus() {
        //查询所有未超时的时限记录，先查村级，再查乡镇
        List<ApprovalTimer> villageTimers = approvalTimerDAO.findTodoByLevel("村级");
        List<ApprovalTimer> townTimers = approvalTimerDAO.findTodoByLevel("乡镇");

        int overdueCount = 0;
        Date now = new Date();

        //检查村级记录是否超时
        if (villageTimers != null && villageTimers.isEmpty()){
            for (ApprovalTimer timer : villageTimers){
                if (timer.getDeadline().before(now)){ //截止时间在当前时间之前，超时
                    approvalTimerDAO.updateOverdueStatus(timer.getTimeId(), 1);//=1超时
                    overdueCount++;
                    System.out.println("村级审批超时：申请ID = " + timer.getAppId() + "截止时间 = " + timer.getDeadline());
                }
            }
        }
        //检查乡镇记录是否超时
        if (townTimers != null && townTimers.isEmpty()){
            for (ApprovalTimer timer : townTimers){
                if (timer.getDeadline().before(now)){ //截止时间在当前时间之前，超时
                    approvalTimerDAO.updateOverdueStatus(timer.getTimeId(), 1);//=1超时
                    overdueCount++;
                    System.out.println("乡镇审批超时：申请ID = " + timer.getAppId() + "截止时间 = " + timer.getDeadline());
                }
            }
        }
        System.out.println("超时检查完成，共发现" + overdueCount + "条超时记录");
        return  overdueCount;
    }

    @Override
    public boolean updateRemindStatus(Integer timerId, String remindStatus) {
        if (timerId == null || timerId <= 0 || remindStatus == null || remindStatus.isEmpty()){
            System.out.println("更新提醒状态失败：时限ID/提醒状态不能为空");
            return false;
        }
        int result = approvalTimerDAO.updateRemindStatus(timerId, remindStatus);
        boolean success = result > 0;
        if (success){
            System.out.println("更新提醒状态成功：时限ID = " + timerId + ",新状态 = " + remindStatus);
        }else {
            System.out.println("更新提醒状态失败");
        }
        return success;
    }

    @Override
    public List<ApprovalTimer> getOverdueTimersByLevel(String level) {
        if (level == null || level.isEmpty()){
            return  null;
        }
        return approvalTimerDAO.findOverdueByLevel(level);
    }

    @Override
    public List<ApprovalTimer> getTodoTimersByLevel(String level) {
        if (level == null || level.isEmpty()){
            return  null;
        }
        return approvalTimerDAO.findTodoByLevel(level);
    }
}
