package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.LandRegistration;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 确权DAO实现类，操作land_registrations表
 */
public class LandRegistrationDAOImpl extends BaseDAO {

    /**
     * 新增确权记录
     * @param landRegistration
     * @return
     */
    public int addLandRegistration(LandRegistration landRegistration){
        String sql = "INSERT INTO LandRegistrations(app_id,cert_no,reg_time,operator_id) VALUES (?,?,?,?)";
        return update(sql,
                landRegistration.getAppId(),
                landRegistration.getCertNo(),
                landRegistration.getRegTime(),
                landRegistration.getOperatorId()
        );
    }

    /**
     * 根据appId查询确权记录
     * @param appId
     * @return
     */
    public LandRegistration getLandRegistrationByAppId(Integer appId){
        String sql = "SELECT * FROM LandRegistrations WHERE app_id = ?";
        return query(sql, new ResultSetHandler<LandRegistration>() {
            @Override
            public LandRegistration handle(ResultSet rs) throws SQLException {
                if (rs.next()){
                    LandRegistration landRegistration = new LandRegistration();
                    landRegistration.setRegId(rs.getInt("reg_id"));
                    landRegistration.setAppId(rs.getInt("app_id"));
                    landRegistration.setCertNo(rs.getString("cert_no"));
                    landRegistration.setRegTime(rs.getDate("reg_time"));
                    landRegistration.setOperatorId(rs.getInt("operator_id"));
                    return landRegistration;
                }
                return null;
            }
        }, appId);
    }

    /**
     * 更新确权证书编号
     * @param regId
     * @param newCertNo
     * @return
     */
    public int updateCertNo(Integer regId,String newCertNo){
        String sql = "UPDATE LandRegistrations SET cert_no = ? WHERE reg_id = ?";
        return update(sql, newCertNo,regId);
    }
}
