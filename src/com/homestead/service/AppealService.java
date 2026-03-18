package com.homestead.service;

import com.homestead.entity.Appeal;

import java.util.List;

public interface AppealService {

    boolean submitAppeal(Appeal appeal);
    boolean handleAppeal(Integer appealId,String reviewResult,String reviewOpinion);
    List<Appeal> getPendingAppeals();
    Appeal getAppealByAppId(Integer appId);
}
