package com.homestead.service.impl;

import com.homestead.dao.impl.LandRegistrationDAOImpl;
import com.homestead.entity.LandRegistration;
import com.homestead.service.LandRegistrationService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

public class LandRegistrationServiceImpl implements LandRegistrationService {
    private LandRegistrationDAOImpl regDAO = new LandRegistrationDAOImpl();

    @Override
    public boolean createLandRegistration(LandRegistration landRegistration) {
        if (landRegistration == null || landRegistration.getAppId() == null || landRegistration.getCertNo() == null) {
            return false;
        }
        if (landRegistration.getRegTime() == null) {
            landRegistration.setRegTime(new Date());
        }
        try {
            int result = regDAO.addLandRegistration(landRegistration);
            return result > 0;
        } catch (RuntimeException e) {
            // 检查异常链中是否包含唯一键冲突
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                // 申请ID已存在，返回 false，由上层显示友好提示
                return false;
            }
            // 其他异常打印堆栈并返回 false
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LandRegistration getLandRegistrationByAppId(Integer appId) {
        if (appId == null || appId <= 0) {
            return null;
        }
        return regDAO.getLandRegistrationByAppId(appId);
    }

    @Override
    public boolean updateCertNo(Integer regId, String newCertNo) {
        if (regId == null || newCertNo == null || newCertNo.trim().isEmpty()) {
            return false;
        }
        int result = regDAO.updateCertNo(regId, newCertNo);
        return result > 0;
    }
}