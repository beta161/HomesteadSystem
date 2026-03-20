package com.homestead.ui;

import com.homestead.entity.PublicNotice;
import com.homestead.entity.User;
import com.homestead.service.PublicNoticeService;
import com.homestead.service.impl.PublicNoticeServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class PublicNoticeFrame extends JPanel {
    private User loginUser;
    private JTextField tfAppId;
    private JTextArea taContent;
    private JTable table;
    private DefaultTableModel tableModel;
    private PublicNoticeService noticeService;

    public PublicNoticeFrame(User user) {
        this.loginUser = user;
        this.noticeService = new PublicNoticeServiceImpl();
        initUI();
        loadNotices();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_WHITE);

        // 顶部发布面板
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("发布公示"));
        topPanel.setBackground(UIUtil.COLOR_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 申请ID
        JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(lblAppId, gbc);

        tfAppId = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 0;
        topPanel.add(tfAppId, gbc);

        // 公示内容
        JLabel lblContent = UIUtil.createLabel("公示内容：", false);
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(lblContent, gbc);

        taContent = new JTextArea(3, 20);
        taContent.setFont(UIUtil.FONT_NORMAL);
        taContent.setLineWrap(true);
        taContent.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1; gbc.gridy = 1;
        topPanel.add(new JScrollPane(taContent), gbc);

        // 发布按钮
        JButton btnPublish = UIUtil.createButton("发布公示");
        btnPublish.addActionListener(e -> publishNotice());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(btnPublish, gbc);

        add(topPanel, BorderLayout.NORTH);

        // 公示列表表格
        String[] columns = {"公示ID", "申请ID", "公示内容", "发布时间", "状态", "操作"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(UIUtil.FONT_SMALL);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 底部刷新按钮
        JButton btnRefresh = UIUtil.createButton("刷新列表");
        btnRefresh.addActionListener(e -> loadNotices());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * 发布公示
     */
    private void publishNotice() {
        String appIdStr = tfAppId.getText().trim();
        String content = taContent.getText().trim();

        if (appIdStr.isEmpty()) {
            UIUtil.showError("请输入申请ID！");
            return;
        }
        if (content.isEmpty()) {
            UIUtil.showError("公示内容不能为空！");
            return;
        }

        try {
            PublicNotice notice = new PublicNotice();
            notice.setAppId(Integer.parseInt(appIdStr));
            notice.setNoticeContent(content);
            notice.setPublishTime(new Date());
            notice.setStatus("公示中");

            boolean success = noticeService.publishNotice(notice);
            if (success) {
                UIUtil.showInfo("公示发布成功！");
                taContent.setText("");
                loadNotices();
            } else {
                UIUtil.showError("发布失败！申请ID可能已存在公示！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID必须为整数！");
        }
    }

    /**
     * 加载公示列表
     */
    private void loadNotices() {
        tableModel.setRowCount(0);
        List<PublicNotice> list = noticeService.getAllPublicNotices();

        if (list == null || list.isEmpty()) {
            tableModel.addRow(new Object[]{"暂无公示记录", "", "", "", "", ""});
            return;
        }

        for (PublicNotice notice : list) {
            Object[] row = new Object[6];
            row[0] = notice.getNoticeId();
            row[1] = notice.getAppId();
            row[2] = notice.getNoticeContent();
            row[3] = notice.getPublishTime().toLocaleString();
            row[4] = notice.getStatus();

            // 结束公示按钮（仅公示中状态显示）
            if ("公示中".equals(notice.getStatus())) {
                JButton btnEnd = UIUtil.createButton("结束公示");
                btnEnd.setPreferredSize(new Dimension(80, 30));
                btnEnd.addActionListener(e -> endNotice(notice.getNoticeId()));
                row[5] = btnEnd;
            } else {
                row[5] = ""; // 已结束则无操作按钮
            }

            tableModel.addRow(row);
        }
    }

    /**
     * 结束公示
     */
    private void endNotice(Integer noticeId) {
        int confirm = UIUtil.showConfirm("确认结束该公示？");
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = noticeService.endNotice(noticeId);
            if (success) {
                UIUtil.showInfo("公示已结束！");
                loadNotices();
            } else {
                UIUtil.showError("操作失败！");
            }
        }
    }
}