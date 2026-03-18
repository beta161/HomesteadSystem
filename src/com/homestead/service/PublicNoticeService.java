package com.homestead.service;

import com.homestead.entity.PublicNotice;

import java.util.List;

public interface PublicNoticeService {
    boolean publishNotice(PublicNotice publicNotice);
    List<PublicNotice> getAllPublicNotices();
    boolean endNotice(Integer noticeId);
    PublicNotice getNoticeByAppId(Integer appId);
}
