package com.homestead.ui;

import com.homestead.entity.PublicNotice;
import com.homestead.entity.User;
import com.homestead.service.PublicNoticeService;
import com.homestead.service.impl.PublicNoticeServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class PublicNoticeFrame extends JPanel {
    private User loginUser;
    private JPanel noticeListPanel;
    private PublicNoticeService noticeService;

    public PublicNoticeFrame(User user) {
        this.loginUser = user;
        this.noticeService = new PublicNoticeServiceImpl();
        initUI();
        loadNotices();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        // 发布区域（管理员可见）
        if ("管理员".equals(loginUser.getRole())) {
            JPanel publishPanel = UIUtil.createTitledPanel("发布新公示");
            publishPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 15, 8, 15);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
            gbc.gridx = 0; gbc.gridy = 0;
            publishPanel.add(lblAppId, gbc);
            JTextField tfAppId = UIUtil.createTextField();
            tfAppId.setPreferredSize(new Dimension(200, 35));
            gbc.gridx = 1;
            publishPanel.add(tfAppId, gbc);

            JLabel lblContent = UIUtil.createLabel("公示内容：", false);
            gbc.gridx = 0; gbc.gridy = 1;
            publishPanel.add(lblContent, gbc);
            JTextArea taContent = new JTextArea(3, 30);
            taContent.setFont(UIUtil.FONT_BODY);
            taContent.setLineWrap(true);
            taContent.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            JScrollPane scroll = new JScrollPane(taContent);
            scroll.setPreferredSize(new Dimension(400, 80));
            gbc.gridx = 1;
            publishPanel.add(scroll, gbc);

            JButton btnPublish = UIUtil.createButton("发布公示");
            btnPublish.addActionListener(e -> {
                String appIdStr = tfAppId.getText().trim();
                String content = taContent.getText().trim();
                if (appIdStr.isEmpty() || content.isEmpty()) {
                    UIUtil.showError("申请ID和公示内容不能为空！");
                    return;
                }
                try {
                    PublicNotice notice = new PublicNotice();
                    notice.setAppId(Integer.parseInt(appIdStr));
                    notice.setNoticeContent(content);
                    notice.setPublishTime(new Date());
                    notice.setStatus("公示中");
                    if (noticeService.publishNotice(notice)) {
                        UIUtil.showInfo("发布成功！");
                        tfAppId.setText("");
                        taContent.setText("");
                        loadNotices();
                    } else {
                        UIUtil.showError("发布失败！");
                    }
                } catch (NumberFormatException ex) {
                    UIUtil.showError("申请ID必须为整数！");
                }
            });
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
            publishPanel.add(btnPublish, gbc);
            card.add(publishPanel, BorderLayout.NORTH);
        }

        // 公示列表（卡片式）
        noticeListPanel = new JPanel();
        noticeListPanel.setLayout(new BoxLayout(noticeListPanel, BoxLayout.Y_AXIS));
        noticeListPanel.setBackground(UIUtil.COLOR_BG);
        JScrollPane scrollPane = new JScrollPane(noticeListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scrollPane, BorderLayout.CENTER);

        add(card);
    }

    private void loadNotices() {
        noticeListPanel.removeAll();
        List<PublicNotice> list = noticeService.getAllPublicNotices();
        if (list != null && !list.isEmpty()) {
            for (PublicNotice notice : list) {
                JPanel card = UIUtil.createCardPanel(new GridBagLayout());
                card.setMaximumSize(new Dimension(800, 140));
                card.setBackground(Color.WHITE);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 15, 8, 15);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // 第一行：标题和状态
                JLabel lblTitle = new JLabel("公示 #" + notice.getNoticeId() + " - 申请ID: " + notice.getAppId());
                lblTitle.setFont(UIUtil.FONT_SUBTITLE);
                lblTitle.setForeground(UIUtil.COLOR_PRIMARY);
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.8;
                card.add(lblTitle, gbc);

                JLabel lblStatus = new JLabel("状态：" + notice.getStatus());
                lblStatus.setFont(UIUtil.FONT_SMALL);
                lblStatus.setForeground("公示中".equals(notice.getStatus()) ? UIUtil.COLOR_SUCCESS : UIUtil.COLOR_TEXT_HINT);
                gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.2;
                gbc.anchor = GridBagConstraints.EAST;
                card.add(lblStatus, gbc);

                // 第二行：公示内容
                JLabel lblContent = new JLabel("<html>" + notice.getNoticeContent() + "</html>");
                lblContent.setFont(UIUtil.FONT_BODY);
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
                gbc.anchor = GridBagConstraints.WEST;
                card.add(lblContent, gbc);

                // 第三行：发布时间
                String timeStr = notice.getPublishTime() != null ? notice.getPublishTime().toLocaleString() : "";
                JLabel lblTime = new JLabel("发布时间：" + timeStr);
                lblTime.setFont(UIUtil.FONT_SMALL);
                lblTime.setForeground(UIUtil.COLOR_TEXT_HINT);
                gbc.gridy = 2;
                card.add(lblTime, gbc);

                // 第四行：操作按钮（仅管理员且公示中）
                if ("管理员".equals(loginUser.getRole()) && "公示中".equals(notice.getStatus())) {
                    JButton btnEnd = UIUtil.createButton("结束公示");
                    btnEnd.setPreferredSize(new Dimension(100, 32));
                    btnEnd.addActionListener(e -> {
                        int confirm = UIUtil.showConfirm("确认结束该公示？");
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (noticeService.endNotice(notice.getNoticeId())) {
                                UIUtil.showInfo("公示已结束！");
                                loadNotices();
                            } else {
                                UIUtil.showError("操作失败！");
                            }
                        }
                    });
                    gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
                    card.add(btnEnd, gbc);
                }

                noticeListPanel.add(card);
                noticeListPanel.add(Box.createVerticalStrut(12));
            }
        } else {
            JLabel lblEmpty = new JLabel("暂无公示记录");
            lblEmpty.setFont(UIUtil.FONT_BODY);
            lblEmpty.setForeground(UIUtil.COLOR_TEXT_HINT);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            noticeListPanel.add(lblEmpty);
        }
        noticeListPanel.revalidate();
        noticeListPanel.repaint();
    }
}