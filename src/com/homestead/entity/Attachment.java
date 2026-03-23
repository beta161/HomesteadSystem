package com.homestead.entity;

import java.util.Date;

/**
 * 附件实体类，attachments表
 */
public class Attachment {
    private Integer attachId;//主键
    private Integer appId;//关联申请id
    private String filePath;
    private String fileName;
    private Date uploadTime;//上传时间
    private String uploader;

    public Attachment() {
    }


    //上传附件用的构造函数
    public Attachment(Integer appId, String filePath, String fileName,Date uploadTime){
        this.appId = appId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uploadTime = uploadTime;
    }
    public Attachment(Integer attachId, Integer appId, String filePath, String fileName, Date uploadTime) {
        this.attachId = attachId;
        this.appId = appId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uploadTime = uploadTime;
    }

    public Integer getAttachId() {
        return attachId;
    }

    public void setAttachId(Integer attachId) {
        this.attachId = attachId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
    public String getUploader() {
        return uploader;
    }
    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
}
