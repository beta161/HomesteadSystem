package com.homestead.service.impl;

import com.homestead.dao.impl.PublicNoticeDAOImpl;
import com.homestead.entity.PublicNotice;
import com.homestead.service.PublicNoticeService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PublicNoticeServiceImpl implements PublicNoticeService {
    private PublicNoticeDAOImpl noticeDAO = new PublicNoticeDAOImpl();
    @Override
    public boolean publishNotice(PublicNotice publicNotice) {
        //参数校验
        if(publicNotice == null || publicNotice.getAppId() == null || publicNotice.getNoticeContent() == null){
            return false;
        }
        //自动填充发布时间和默认状态
        if(publicNotice.getPublishTime() == null){
            publicNotice.setPublishTime(new Date());
        }
        if (publicNotice.getStatus() == null){
            publicNotice.setStatus("公式中");
        }
        int result = noticeDAO.addPublicNotice(publicNotice);
        return result > 0;
    }

    @Override
    public List<PublicNotice> getAllPublicNotices() {
        return noticeDAO.getAllPublicNotices();
    }

    @Override
    public boolean endNotice(Integer noticeId) {
        if (noticeId == null || noticeId <= 0){
            return false;
        }
        int result = noticeDAO.updateNoticeStatus(noticeId,"已结束");
        return result > 0;
    }

    @Override
    public PublicNotice getNoticeByAppId(Integer appId) {
        if (appId == null || appId <= 0){
            return null;
        }
        return noticeDAO.getNoticeByAppId(appId);
    }
}
