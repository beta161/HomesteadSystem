package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.PublicNotice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 公示DAO实现类，操作public_notices表
 */
public class PublicNoticeDAOImpl extends BaseDAO {

    /**
     * 新增公示记录
     * @param publicNotice
     * @return
     */
    public int addPublicNotice(PublicNotice publicNotice){
        String sql = "INSERT INTO public_notices(app_id,notice_content,publish_time,status) VALUES (?,?,?,?)";
        return update(sql,
                publicNotice.getAppId(),
                publicNotice.getNoticeContent(),
                publicNotice.getPublishTime(),
                publicNotice.getStatus()
        );
    }


    /**
     * 查询所有公示记录
     * @return
     */
    public List<PublicNotice> getAllPublicNotices(){
        String sql = "SELECT * FROM PublicNotices ORDER BY publish_time DESC";
        return query(sql, new ResultSetHandler<List<PublicNotice>>() {
            @Override
            public List<PublicNotice> handle(ResultSet rs) throws SQLException {
                List<PublicNotice> list = new ArrayList<>();
                while (rs.next()){
                    PublicNotice notice = new PublicNotice();
                    notice.setNoticeId(rs.getInt("notice_id"));
                    notice.setAppId(rs.getInt("app_id"));
                    notice.setNoticeContent(rs.getString("notice_content"));
                    notice.setPublishTime(rs.getDate("publish_time"));
                    notice.setStatus(rs.getString("status"));
                    list.add(notice);
                }
                return list;
            }
        });
    }

    /**
     * 更新公示状态
     * @param noticeId
     * @param status
     * @return
     */
    public int updateNoticeStatus(Integer noticeId, String status){
        String sql = "UPDATE public_notices SET status = ? WHERE notice_id = ?";
        return update(sql, status, noticeId);
    }

    /**
     * 根据appId查询公示记录
     * @param appId
     * @return
     */
    public PublicNotice getNoticeByAppId(Integer appId){
        String sql = "SELECT * FROM public_notices WHERE app_id = ?";
        return query(sql, new ResultSetHandler<PublicNotice>() {
            @Override
            public PublicNotice handle(ResultSet rs) throws SQLException {
                if (rs.next()){
                    PublicNotice notice = new PublicNotice();
                    notice.setNoticeId(rs.getInt("notice_id"));
                    notice.setAppId(rs.getInt("app_id"));
                    notice.setNoticeContent(rs.getString("notice_content"));
                    notice.setPublishTime(rs.getDate("publish_time"));
                    notice.setStatus(rs.getString("status"));
                    return notice;
                }
                return null;
            }
        },appId);
    }
}
