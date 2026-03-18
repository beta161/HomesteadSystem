package com.homestead.service;

import com.homestead.entity.SystemLog;

import java.util.List;

public interface SystemLogService {
    boolean log(Integer userId,String operation,String detail);
    List<SystemLog> getAllLogs();
    List<SystemLog> getLogsByUserId(Integer userId);
}
