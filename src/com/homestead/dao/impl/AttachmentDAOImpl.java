package com.homestead.dao.impl;

import com.homestead.dao.BaseDAO;
import com.homestead.entity.Attachment;
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 附件DAO实现类，操作attachments表
 */
public class AttachmentDAOImpl extends BaseDAO {

    /**
     * 新增附件记录
     * @param attachment
     * @return
     */
    public int addAttachment(Attachment attachment){
        String sql = "INSERT INTO Attachments(app_id,file_path,file_name,upload_time) VALUES (?,?,?,?)";
        return update(sql,
                attachment.getAppId(),
                attachment.getFilePath(),
                attachment.getFileName(),
                attachment.getUploadTime());
    }

    /**
     * 根据appId查询附件列表
     * @param appId
     * @return
     */
    public List<Attachment> getAttachmentsByAppId(Integer appId){
        String sql = "SELECT * FROM Attachments WHERE app_id = ?";
        return query(sql, new ResultSetHandler<List<Attachment>>() {
            @Override
            public List<Attachment> handle(ResultSet rs) throws SQLException {
                List<Attachment> list = new ArrayList<>();
                while (rs.next()){
                    Attachment attach = new Attachment();
                    attach.setAttachId(rs.getInt("attach_id"));
                    attach.setAppId(rs.getInt("app_id"));
                    attach.setFilePath(rs.getString("file_path"));
                    attach.setFileName(rs.getString("file_name"));
                    attach.setUploadTime(rs.getDate("upload_time"));
                    list.add(attach);
                }
                return list;
            }
        }, appId);
    }

    /**
     * 删除附件记录,根据附件id
     * @param attachId
     * @return
     */
    public int deleteAttachment (Integer attachId){
        String sql = "DELETE FROM Attachments WHERE attach_id = ?";
        return update(sql, attachId);
    }
}
