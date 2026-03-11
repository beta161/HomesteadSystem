package com.homestead.entity;

import java.util.Date;

public class ApprovalTimer {
    private Integer timeId;//时限记录id（主）
    private Integer appId;//申请id（关联application表）
    private String approvalLevel;//审批级别：村级/乡镇
    private Date startTime;//审批开始时间
    private Date deadline;//审批截止时间
    private Integer isOverdue;//是否超时，0未，1超
    private String remindStatus;//提醒状态：未提醒/已提醒
    private Date createTime;//记录创建时间

    public ApprovalTimer() {
    }

    public ApprovalTimer(Integer timeId, Integer appId, String approvalLevel, Date startTime, Date deadline, Integer isOverdue, String remindStatus, Date createTime) {
        this.timeId = timeId;
        this.appId = appId;
        this.approvalLevel = approvalLevel;
        this.startTime = startTime;
        this.deadline = deadline;
        this.isOverdue = isOverdue;
        this.remindStatus = remindStatus;
        this.createTime = createTime;
    }

    //常用，初始化审批时用

    public ApprovalTimer(Integer appId, String approvalLevel, Date startTime, Date deadline, Integer isOverdue, String remindStatus) {
        this.appId = appId;
        this.approvalLevel = approvalLevel;
        this.startTime = startTime;
        this.deadline = deadline;
        this.isOverdue = 0;//默认未超时
        this.remindStatus = "未提醒"; //默认
    }

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }

    public String getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(String remindStatus) {
        this.remindStatus = remindStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ApprovalTimer{" +
                "timeId=" + timeId +
                ", appId=" + appId +
                ", approvalLevel='" + approvalLevel + '\'' +
                ", deadline=" + deadline +
                ", isOverdue=" + isOverdue +
                ", remindStatus='" + remindStatus + '\'' +
                '}';
    }
}
