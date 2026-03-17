package com.homestead.entity;


import java.util.Date;

/**
 * 确权登记实体类，LandRegistration表
 */
public class LandRegistration {

    private  Integer regId;//主键
    private  Integer appId;//关联申请id
    private  String certNo;//证书编号
    private Date regTime;//登记时间
    private  Integer operatorId;//操作人id

    public LandRegistration() {
    }
    //新增确权记录用的构造方法
    public LandRegistration(Integer appId, String certNo, Date regTime, Integer operatorId) {
        this.appId = appId;
        this.certNo = certNo;
        this.regTime = regTime;
        this.operatorId = operatorId;
    }

    public LandRegistration(Integer regId, Integer appId, String certNo, Date regTime, Integer operatorId) {
        this.regId = regId;
        this.appId = appId;
        this.certNo = certNo;
        this.regTime = regTime;
        this.operatorId = operatorId;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}
