package com.homestead.entity;

import java.util.Date;

public class ApprovalRecord {
    private Integer recordId;//记录id（主）
    private Integer appId;//申请id（关联application表）
    private Integer approverId;//审批人id（关联user表）
    private String level;//审批级别：村级/镇级
    private String opinion;//审批意见
    private String result;//审批结果：通过/驳回
    private Date approveTime;//审批时间

    public ApprovalRecord() {
    }


    public ApprovalRecord(Integer recordId, Integer appId, Integer approverId, String opinion, String level, String result, Date approveTime) {
        this.recordId = recordId;
        this.appId = appId;
        this.approverId = approverId;
        this.opinion = opinion;
        this.level = level;
        this.result = result;
        this.approveTime = approveTime;
    }


    //常用构造方法，审批时创建记录

    public ApprovalRecord(Integer appId, Integer approverId, String level, String opinion, String result) {
        this.appId = appId;
        this.approverId = approverId;
        this.level = level;
        this.opinion = opinion;
        this.result = result;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    @Override
    public String toString() {
        return "ApprovalRecord{" +
                "recordId=" + recordId +
                ", appId=" + appId +
                ", level='" + level + '\'' +
                ", result='" + result + '\'' +
                ", opinion='" + opinion + '\'' +
                '}';
    }
}
