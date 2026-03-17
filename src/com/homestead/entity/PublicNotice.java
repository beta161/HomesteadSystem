package com.homestead.entity;

import java.util.Date;

/**
 * 公示实体类，对应publicNotice表
 */
public class PublicNotice {
    private Integer noticeId;//主键
    private Integer appId;//关联申请id
    private String noticeContent;//公示内容
    private Date publishTime;//发布时间
    private String status;//状态，公示中/已结束

    public PublicNotice() {
    }

    //发布公示用的构造方法
    public PublicNotice(Integer appId, String noticeContent, Date publishTime, String status) {
        this.appId = appId;
        this.noticeContent = noticeContent;
        this.publishTime = publishTime;
        this.status = status;
    }

    public PublicNotice(Integer noticeId, Integer appId, String noticeContent, Date publishTime, String status) {
        this.noticeId = noticeId;
        this.appId = appId;
        this.noticeContent = noticeContent;
        this.publishTime = publishTime;
        this.status = status;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
