package com.homestead.ui;

import com.homestead.entity.Attachment;
import com.homestead.entity.User;
import com.homestead.service.AttachmentService;
import com.homestead.service.impl.AttachmentServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.List;

public class AttachmentFrame extends JPanel {
    private User loginUser;
    private JTextField tfAppId;
    private JTextField tfFilePath;
    private JTable table;
    private DefaultTableModel tableModel;
    private AttachmentService attachService;

    public AttachmentFrame(User user) {
        this.loginUser = user;
        this.attachService = new AttachmentServiceImpl();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        // 上传区域
        JPanel uploadPanel = UIUtil.createTitledPanel("附件上传");
        uploadPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
        gbc.gridx = 0; gbc.gridy = 0;
        uploadPanel.add(lblAppId, gbc);
        tfAppId = UIUtil.createTextField();
        tfAppId.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        uploadPanel.add(tfAppId, gbc);

        JLabel lblFile = UIUtil.createLabel("文件路径：", false);
        gbc.gridx = 0; gbc.gridy = 1;
        uploadPanel.add(lblFile, gbc);
        JPanel filePanel = new JPanel(new BorderLayout());
        tfFilePath = UIUtil.createTextField();
        JButton btnSelect = UIUtil.createButton("浏览");
        btnSelect.setPreferredSize(new Dimension(80, 35));
        btnSelect.addActionListener(e -> selectFile());
        filePanel.add(tfFilePath, BorderLayout.CENTER);
        filePanel.add(btnSelect, BorderLayout.EAST);
        gbc.gridx = 1;
        uploadPanel.add(filePanel, gbc);

        JButton btnUpload = UIUtil.createButton("上传附件");
        btnUpload.addActionListener(e -> uploadAttachment());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        uploadPanel.add(btnUpload, gbc);

        card.add(uploadPanel, BorderLayout.NORTH);

        // 附件列表区域
        String[] columns = {"附件ID", "申请ID", "文件名", "文件路径", "上传时间", "操作"};
        tableModel = new DefaultTableModel(columns, 0);
        table = UIUtil.createTable();
        table.setModel(tableModel);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // 底部按钮
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRefresh = UIUtil.createButton("刷新列表");
        btnRefresh.addActionListener(e -> loadAttachments());
        JButton btnDelete = UIUtil.createButton("删除选中");
        btnDelete.addActionListener(e -> deleteSelectedAttachment());
        btnPanel.add(btnRefresh);
        btnPanel.add(btnDelete);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void selectFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            tfFilePath.setText(file.getAbsolutePath());
        }
    }

    private void uploadAttachment() {
        String appIdStr = tfAppId.getText().trim();
        String path = tfFilePath.getText().trim();

        if (appIdStr.isEmpty()) {
            UIUtil.showError("请输入申请ID！");
            return;
        }
        if (path.isEmpty()) {
            UIUtil.showError("请选择文件！");
            return;
        }

        try {
            Attachment attach = new Attachment();
            attach.setAppId(Integer.parseInt(appIdStr));
            attach.setFilePath(path);
            attach.setFileName(new File(path).getName());
            attach.setUploadTime(new Date());

            boolean success = attachService.uploadAttachment(attach);
            if (success) {
                UIUtil.showInfo("附件上传成功！");
                tfFilePath.setText("");
                loadAttachments();
            } else {
                UIUtil.showError("上传失败！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID必须为整数！");
        }
    }

    private void loadAttachments() {
        String appIdStr = tfAppId.getText().trim();
        if (appIdStr.isEmpty()) {
            UIUtil.showError("请输入申请ID！");
            return;
        }

        try {
            List<Attachment> list = attachService.getAttachmentsByAppId(Integer.parseInt(appIdStr));
            tableModel.setRowCount(0);
            if (list != null && !list.isEmpty()) {
                for (Attachment attach : list) {
                    String timeStr = attach.getUploadTime() != null ? attach.getUploadTime().toLocaleString() : "";
                    tableModel.addRow(new Object[]{
                            attach.getAttachId(),
                            attach.getAppId(),
                            attach.getFileName(),
                            attach.getFilePath(),
                            timeStr,
                            "删除"
                    });
                }
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID格式错误！");
        }
    }

    private void deleteSelectedAttachment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtil.showError("请先选择要删除的附件！");
            return;
        }
        Integer attachId = (Integer) tableModel.getValueAt(row, 0);
        int confirm = UIUtil.showConfirm("确认删除该附件？");
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = attachService.deleteAttachment(attachId);
            if (success) {
                UIUtil.showInfo("删除成功！");
                loadAttachments();
            } else {
                UIUtil.showError("删除失败！");
            }
        }
    }
}