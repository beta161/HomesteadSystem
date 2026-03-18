package com.homestead.service.impl;

import com.homestead.dao.impl.LandRegistrationDAOImpl;
import com.homestead.entity.LandRegistration;
import com.homestead.service.LandRegistrationService;

import java.util.Date;

public class LandRegistrationServiceImpl implements LandRegistrationService{
    private LandRegistrationDAOImpl regDAO = new LandRegistrationDAOImpl();
    @Override
    public boolean createLandRegistration(LandRegistration landRegistration) {
        //入参校验
        if (landRegistration == null || landRegistration.getAppId() == null || landRegistration.getCertNo() == null){
            return false;
        }
        //自动填充登记时间
        if (landRegistration.getRegTime() == null){
            landRegistration.setRegTime(new Date());
        }
        int result = regDAO.addLandRegistration(landRegistration);
        return result > 0;
    }

    @Override
    public LandRegistration getLandRegistrationByAppId(Integer appId) {
        if (appId == null || appId <= 0){
            return null;
        }
        return regDAO.getLandRegistrationByAppId(appId);
    }

    @Override
    public boolean updateCertNo(Integer regId, String newCertNo) {
        if (regId == null  || newCertNo == null || newCertNo.trim().isEmpty()){
            return false;
        }
        int result = regDAO.updateCertNo(regId,newCertNo);
        return result > 0;
    }
}
