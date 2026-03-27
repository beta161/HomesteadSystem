package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StatisticsFrame extends JPanel {
    private User loginUser;
    private ApplicationService appService;

    private int total = 0;
    private int approved = 0;
    private int pending = 0;      // 待审批（村级+乡镇）
    private int rejected = 0;

    public StatisticsFrame(User user) {
        this.loginUser = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
        loadStatistics();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        JLabel lblTitle = UIUtil.createLabel("统计报表", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        card.add(lblTitle, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIUtil.FONT_BODY);
        tabbedPane.setBackground(Color.WHITE);

        JPanel overviewPanel = createOverviewPanel();
        tabbedPane.addTab("概览", overviewPanel);

        JPanel approvalPanel = createApprovalTablePanel();
        tabbedPane.addTab("审批环节统计", approvalPanel);

        JPanel monthlyPanel = createMonthlyTrendPanel();
        tabbedPane.addTab("月度趋势", monthlyPanel);

        card.add(tabbedPane, BorderLayout.CENTER);
        add(card);
    }

    private void loadStatistics() {
        List<Application> all = appService.getAllApplications();
        if (all == null || all.isEmpty()) {
            total = 0;
            approved = 0;
            pending = 0;
            rejected = 0;
            return;
        }
        total = all.size();
        approved = (int) all.stream().filter(app -> "已批准".equals(app.getStatus())).count();
        rejected = (int) all.stream().filter(app -> "已驳回".equals(app.getStatus())).count();
        pending = (int) all.stream().filter(app -> "待村级审批".equals(app.getStatus()) || "待乡镇审批".equals(app.getStatus())).count();
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtil.COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        metricsPanel.setBackground(UIUtil.COLOR_BG);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        metricsPanel.add(createMetricCard("总申请数", String.valueOf(total), UIUtil.COLOR_PRIMARY));
        metricsPanel.add(createMetricCard("已批准", String.valueOf(approved), UIUtil.COLOR_SUCCESS));
        metricsPanel.add(createMetricCard("已驳回", String.valueOf(rejected), UIUtil.COLOR_DANGER));

        panel.add(metricsPanel, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth() - 100;
                int height = getHeight() - 80;
                int barWidth = 80;
                int startX = 80;
                int baseY = getHeight() - 50;

                if (total == 0) {
                    g2.setColor(UIUtil.COLOR_TEXT_HINT);
                    g2.drawString("暂无数据", startX + barWidth, baseY - 30);
                    return;
                }

                int approvedHeight = (int) ((double) approved / total * height);
                int pendingHeight = (int) ((double) pending / total * height);
                int rejectedHeight = (int) ((double) rejected / total * height);

                g2.setColor(UIUtil.COLOR_SUCCESS);
                g2.fillRect(startX, baseY - approvedHeight, barWidth, approvedHeight);
                g2.setColor(UIUtil.COLOR_WARNING);
                g2.fillRect(startX + barWidth + 30, baseY - pendingHeight, barWidth, pendingHeight);
                g2.setColor(UIUtil.COLOR_DANGER);
                g2.fillRect(startX + 2 * (barWidth + 30), baseY - rejectedHeight, barWidth, rejectedHeight);

                g2.setColor(UIUtil.COLOR_SUCCESS);
                g2.fillRect(30, 30, 15, 15);
                g2.setColor(UIUtil.COLOR_TEXT_BODY);
                g2.drawString("已批准", 50, 42);
                g2.setColor(UIUtil.COLOR_WARNING);
                g2.fillRect(30, 55, 15, 15);
                g2.drawString("待审批", 50, 67);
                g2.setColor(UIUtil.COLOR_DANGER);
                g2.fillRect(30, 80, 15, 15);
                g2.drawString("已驳回", 50, 92);

                g2.setColor(UIUtil.COLOR_TEXT_HINT);
                g2.drawString("已批准", startX + barWidth/2 - 15, baseY + 20);
                g2.drawString("待审批", startX + barWidth + 30 + barWidth/2 - 15, baseY + 20);
                g2.drawString("已驳回", startX + 2*(barWidth+30) + barWidth/2 - 15, baseY + 20);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_BORDER));
        chartPanel.setPreferredSize(new Dimension(0, 300));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createApprovalTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtil.COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Application> all = appService.getAllApplications();
        int villageCount = 0, townCount = 0;
        if (all != null) {
            for (Application app : all) {
                if ("待村级审批".equals(app.getStatus())) villageCount++;
                else if ("待乡镇审批".equals(app.getStatus())) townCount++;
            }
        }

        String[] columns = {"审批环节", "待审批数量", "占比"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = UIUtil.createTable();
        table.setModel(model);

        if (total > 0) {
            model.addRow(new Object[]{"村级审批", villageCount, String.format("%.1f%%", (double) villageCount / total * 100)});
            model.addRow(new Object[]{"乡镇审批", townCount, String.format("%.1f%%", (double) townCount / total * 100)});
            model.addRow(new Object[]{"已批准", approved, String.format("%.1f%%", (double) approved / total * 100)});
            model.addRow(new Object[]{"已驳回", rejected, String.format("%.1f%%", (double) rejected / total * 100)});
        } else {
            model.addRow(new Object[]{"暂无数据", 0, "0%"});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMonthlyTrendPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtil.COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 注意：月度趋势需要按月份分组统计，这里暂时显示示例数据，您可后续扩展
        String[] columns = {"月份", "申请总数", "批准数", "驳回数"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = UIUtil.createTable();
        table.setModel(model);

        // 示例数据（实际请调用统计方法）
        model.addRow(new Object[]{"2024-10", 15, 12, 3});
        model.addRow(new Object[]{"2024-11", 22, 18, 4});
        model.addRow(new Object[]{"2024-12", 18, 15, 3});
        model.addRow(new Object[]{"2025-01", 25, 20, 5});
        model.addRow(new Object[]{"2025-02", 20, 17, 3});
        model.addRow(new Object[]{"2025-03", 30, 25, 5});

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel lblNote = new JLabel("注：月度趋势数据为示例，实际请扩展统计方法。");
        lblNote.setFont(UIUtil.FONT_SMALL);
        lblNote.setForeground(UIUtil.COLOR_TEXT_HINT);
        lblNote.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(lblNote, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMetricCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtil.FONT_SMALL);
        lblTitle.setForeground(UIUtil.COLOR_TEXT_HINT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("微软雅黑", Font.BOLD, 28));
        lblValue.setForeground(color);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}