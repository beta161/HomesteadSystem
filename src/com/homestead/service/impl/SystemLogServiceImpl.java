package com.homestead.service.impl;

import com.homestead.dao.impl.SystemLogDAOImpl;
import com.homestead.entity.SystemLog;
import com.homestead.service.SystemLogService;

import java.util.Collections;
import java.util.List;

public class SystemLogServiceImpl implements SystemLogService {
    private SystemLogDAOImpl logDAO = new SystemLogDAOImpl();
    @Override
    public boolean log(Integer userId, String operation, String detail) {
        //参数校验
        if(userId == null || operation == null || detail == null){
            return false;
        }
        SystemLog log = new SystemLog(userId, operation, detail);
        int result = logDAO.addSystemLog(log);
        return result > 0;
    }

    @Override
    public List<SystemLog> getAllLogs() {
        return logDAO.getAllLogsOrdersByTime();
    }

    @Override
    public List<SystemLog> getLogsByUserId(Integer userId) {
        if (userId == null || userId < 0){
            return null;
        }
        return logDAO.getLogsByUserId(userId);
    }
}
