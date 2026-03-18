package com.homestead.service;

import com.homestead.entity.Attachment;

import java.util.List;

public interface AttachmentService {
    boolean uploadAttachment (Attachment  attachment);
    List<Attachment> getAttachmentsByAppId (Integer appId);
    boolean deleteAttachment (Integer attachId);
}
