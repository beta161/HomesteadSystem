package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;

public class StatisticsFrame extends JPanel {
    private User loginUser;
    private ApplicationService appService;

    public StatisticsFrame(User user) {
        this.loginUser = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        JLabel lblTitle = UIUtil.createLabel("统计报表", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        card.add(lblTitle, BorderLayout.NORTH);

        // 简单柱状图（模拟）
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 模拟数据（实际应从数据库获取）
                int total = 120;
                int approved = 80;
                int pending = 30;
                int rejected = 10;

                int width = getWidth() - 60;
                int height = getHeight() - 60;
                int barWidth = 60;
                int startX = 40;
                int baseY = getHeight() - 40;

                // 绘制柱状图
                g2.setColor(UIUtil.COLOR_SUCCESS);
                int approvedHeight = (int) ((double) approved / total * height);
                g2.fillRect(startX, baseY - approvedHeight, barWidth, approvedHeight);
                g2.setColor(UIUtil.COLOR_WARNING);
                int pendingHeight = (int) ((double) pending / total * height);
                g2.fillRect(startX + barWidth + 20, baseY - pendingHeight, barWidth, pendingHeight);
                g2.setColor(UIUtil.COLOR_DANGER);
                int rejectedHeight = (int) ((double) rejected / total * height);
                g2.fillRect(startX + 2 * (barWidth + 20), baseY - rejectedHeight, barWidth, rejectedHeight);

                // 图例
                g2.setColor(UIUtil.COLOR_SUCCESS);
                g2.fillRect(30, 20, 15, 15);
                g2.setColor(UIUtil.COLOR_TEXT_BODY);
                g2.drawString("已批准", 50, 32);
                g2.setColor(UIUtil.COLOR_WARNING);
                g2.fillRect(30, 45, 15, 15);
                g2.drawString("待审批", 50, 57);
                g2.setColor(UIUtil.COLOR_DANGER);
                g2.fillRect(30, 70, 15, 15);
                g2.drawString("已驳回", 50, 82);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        card.add(chartPanel, BorderLayout.CENTER);

        add(card);
    }
}