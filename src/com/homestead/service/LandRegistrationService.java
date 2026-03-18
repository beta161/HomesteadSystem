package com.homestead.service;

import com.homestead.entity.LandRegistration;

public interface LandRegistrationService {
    boolean createLandRegistration(LandRegistration landRegistration);
    LandRegistration getLandRegistrationByAppId(Integer appId);
    boolean updateCertNo(Integer regId,String newCertNo);
}
