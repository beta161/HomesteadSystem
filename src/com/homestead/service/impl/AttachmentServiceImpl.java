package com.homestead.service.impl;

import com.homestead.dao.impl.AttachmentDAOImpl;
import com.homestead.entity.Attachment;
import com.homestead.service.AttachmentService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AttachmentServiceImpl implements AttachmentService {
    private AttachmentDAOImpl attachDAO = new AttachmentDAOImpl();
    @Override
    public boolean uploadAttachment(Attachment attachment) {
        //参数校验
        if(attachment == null || attachment.getAppId() == null || attachment.getFilePath() == null ){
            return false;
        }
        //自动填充上传时间
        if (attachment.getUploadTime() == null){
            attachment.setUploadTime(new Date());
        }
        int result = attachDAO.addAttachment(attachment);
        return result > 0;
    }

    @Override
    public List<Attachment> getAttachmentsByAppId(Integer appId) {
        if (appId == null || appId < 0){
            return null;
        }
        return attachDAO.getAttachmentsByAppId(appId);
    }

    @Override
    public boolean deleteAttachment(Integer attachId) {
        if (attachId == null || attachId < 0){
            return false;
        }
        int result = attachDAO.deleteAttachment(attachId);
        return result > 0;
    }
}
