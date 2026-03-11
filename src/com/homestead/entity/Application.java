package com.homestead.entity;

import java.util.Date;

public class Application {
    private Integer appId;//申请id（主）
    private Integer userId;//申请人id（关联user表）
    private Double plotArea;//地块面积
    private String purpose;//用途
    private String status;//申请状态：待村级初审/初审通过/待乡镇复审/已批准/已驳回
    private Date applyTime;//申请时间
    private Integer attachCount;//附件数量
    private String currentApprovalLevel;//当前审批环节


    public Application() {
    }

    public Application(Integer appId, Integer userId, Double plotArea, String purpose, String status, Date applyTime, Integer attachCount, String currentApprovalLevel) {
        this.appId = appId;
        this.userId = userId;
        this.plotArea = plotArea;
        this.purpose = purpose;
        this.status = status;
        this.applyTime = applyTime;
        this.attachCount = attachCount;
        this.currentApprovalLevel = currentApprovalLevel;
    }

    public Application(Integer userId, Double plotArea, String purpose, String status, Integer attachCount) {
        this.userId = userId;
        this.plotArea = plotArea;
        this.purpose = purpose;
        this.status = "待村级审批";//默认状态
        this.attachCount = 0;//默认无附件
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getPlotArea() {
        return plotArea;
    }

    public void setPlotArea(Double plotArea) {
        this.plotArea = plotArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(Integer attachCount) {
        this.attachCount = attachCount;
    }

    public String getCurrentApprovalLevel() {
        return currentApprovalLevel;
    }

    public void setCurrentApprovalLevel(String currentApprovalLevel) {
        this.currentApprovalLevel = currentApprovalLevel;
    }

    @Override
    public String toString() {
        return "Application{" +
                "appId=" + appId +
                ", userId=" + userId +
                ", plotArea=" + plotArea +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", currentApprovalLevel='" + currentApprovalLevel + '\'' +
                '}';
    }
}


