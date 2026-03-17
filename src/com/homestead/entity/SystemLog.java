package com.homestead.entity;


import java.util.Date;

/**
 * 系统日志实体类，systemlogs表
 */
public class SystemLog {
    private Integer logId;//日志id，主
    private Integer userId;//操作人id
    private String operation;//操作类型，登录/提交申请，审批，确权，公示，申诉
    private String detail;
    private Date opTime;//操作时间

    public SystemLog() {
    }

    //新增日志用的构造方法
    public SystemLog(Integer userId, String operation, String detail) {
        this.userId = userId;
        this.operation = operation;
        this.detail = detail;
    }

    public SystemLog(Integer logId, Integer userId, String operation, String detail, Date opTime) {
        this.logId = logId;
        this.userId = userId;
        this.operation = operation;
        this.detail = detail;
        this.opTime = opTime;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
