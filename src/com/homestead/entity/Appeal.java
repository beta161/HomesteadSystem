package com.homestead.entity;

import java.util.Date;

/**
 * 申诉实体类，appeals表
 */
public class Appeal {
    private Integer appealId;//主键
    private Integer appId;//关联申请id
    private String appealReason;
    private Date appealTime;
    private String reviewResult;//审核结果，枚举：通过/驳回
    private String reviewOpinion;

    public Appeal() {
    }

    //新增申诉用的构造方法
    public Appeal(Integer appId, String appealReason,Date appealTime){
        this.appId = appId;
        this.appealReason = appealReason;
        this.appealTime = appealTime;
    }
    public Appeal(Integer appealId, Integer appId, String appealReason, String reviewResult, Date appealTime, String reviewOpinion) {
        this.appealId = appealId;
        this.appId = appId;
        this.appealReason = appealReason;
        this.reviewResult = reviewResult;
        this.appealTime = appealTime;
        this.reviewOpinion = reviewOpinion;
    }

    public Integer getAppealId() {
        return appealId;
    }

    public void setAppealId(Integer appealId) {
        this.appealId = appealId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public Date getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(Date appealTime) {
        this.appealTime = appealTime;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewOpinion() {
        return reviewOpinion;
    }

    public void setReviewOpinion(String reviewOpinion) {
        this.reviewOpinion = reviewOpinion;
    }
}
