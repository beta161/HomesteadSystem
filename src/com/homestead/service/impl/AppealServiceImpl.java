package com.homestead.service.impl;

import com.homestead.dao.impl.AppealDAOImpl;
import com.homestead.entity.Appeal;
import com.homestead.service.AppealService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AppealServiceImpl implements AppealService {
    private AppealDAOImpl appealDAO = new AppealDAOImpl();
    @Override
    public boolean submitAppeal(Appeal appeal) {
        //参数校验
        if(appeal == null || appeal.getAppId() == null || appeal.getAppealReason() == null){
            return false;
        }
        //自动填充申诉时间
        if (appeal.getAppealTime() == null){
            appeal.setAppealTime(new Date());
        }
        int result = appealDAO.addAppeal(appeal);
        return result > 0;
    }

    @Override
    public boolean handleAppeal(Integer appealId, String reviewResult, String reviewOpinion) {
        if (appealId == null || reviewResult == null){
            return false;
        }
        int result = appealDAO.handleAppeal(appealId,reviewResult,reviewOpinion);
        return result > 0;
    }

    @Override
    public List<Appeal> getPendingAppeals() {
        return appealDAO.getPendingAppeals();
    }

    @Override
    public Appeal getAppealByAppId(Integer appId) {
        if (appId == null || appId <= 0){
            return null;
        }
        return appealDAO.getAppealByAppId(appId);
    }
}
