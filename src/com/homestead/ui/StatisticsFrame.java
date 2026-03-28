package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StatisticsFrame extends JPanel {
    private User loginUser;
    private ApplicationService appService;

    private int total = 0;
    private int approved = 0;
    private int pending = 0;
    private int rejected = 0;

    private List<Object[]> monthlyData;

    private JComboBox<String> cmbTime;
    private JComboBox<String> cmbRegion;
    private JComboBox<String> cmbStatus;
    private JButton btnQuery;
    private JButton btnRefresh;
    private JButton btnExport;
    private JButton btnPrint;

    private JLabel lblTotal, lblApproved, lblPending, lblRejected;
    private JPanel monthlyChartPanel, pieChartPanel;

    public StatisticsFrame(User user) {
        this.loginUser = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtil.COLOR_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ==================== 顶部筛选栏 ====================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        filterPanel.add(new JLabel("按时间："));
        cmbTime = new JComboBox<>(new String[]{"本月", "本年", "全部"});
        cmbTime.setFont(UIUtil.FONT_BODY);
        filterPanel.add(cmbTime);


        filterPanel.add(new JLabel("按状态："));
        cmbStatus = new JComboBox<>(new String[]{"全部", "已通过", "审核中", "驳回"});
        cmbStatus.setFont(UIUtil.FONT_BODY);
        filterPanel.add(cmbStatus);

        btnQuery = UIUtil.createButton("查询");
        btnQuery.addActionListener(e -> loadData());
        filterPanel.add(btnQuery);

        btnRefresh = UIUtil.createButton("刷新");
        btnRefresh.addActionListener(e -> loadData());
        filterPanel.add(btnRefresh);

        btnExport = UIUtil.createButton("导出Excel");
        btnExport.addActionListener(e -> exportToCSV());
        filterPanel.add(btnExport);

        btnPrint = UIUtil.createButton("打印");
        btnPrint.addActionListener(e -> JOptionPane.showMessageDialog(this, "打印功能待实现", "提示", JOptionPane.INFORMATION_MESSAGE));
        filterPanel.add(btnPrint);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // ==================== 统计卡片 ====================
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(UIUtil.COLOR_BG);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel cardTotal = createStatCard("总申请量", "0", UIUtil.COLOR_PRIMARY);
        JPanel cardApproved = createStatCard("已通过", "0", UIUtil.COLOR_SUCCESS);
        JPanel cardPending = createStatCard("审核中", "0", UIUtil.COLOR_WARNING);
        JPanel cardRejected = createStatCard("驳回量", "0", UIUtil.COLOR_DANGER);

        lblTotal = (JLabel) ((BorderLayout) cardTotal.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblApproved = (JLabel) ((BorderLayout) cardApproved.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblPending = (JLabel) ((BorderLayout) cardPending.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblRejected = (JLabel) ((BorderLayout) cardRejected.getLayout()).getLayoutComponent(BorderLayout.CENTER);

        cardsPanel.add(cardTotal);
        cardsPanel.add(cardApproved);
        cardsPanel.add(cardPending);
        cardsPanel.add(cardRejected);

        mainPanel.add(cardsPanel, BorderLayout.NORTH);

        // ==================== 图表区域 ====================
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(UIUtil.COLOR_BG);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        monthlyChartPanel = new JPanel(new BorderLayout());
        monthlyChartPanel.setBackground(Color.WHITE);
        monthlyChartPanel.setBorder(BorderFactory.createTitledBorder("月度申请量趋势"));
        chartsPanel.add(monthlyChartPanel);

        pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBackground(Color.WHITE);
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("审批结果占比"));
        chartsPanel.add(pieChartPanel);

        mainPanel.add(chartsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createStatCard(String title, String initialValue, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtil.FONT_SMALL);
        lblTitle.setForeground(UIUtil.COLOR_TEXT_HINT);
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("微软雅黑", Font.BOLD, 28));
        lblValue.setForeground(color);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void loadData() {
        // 统计数据
        List<com.homestead.entity.Application> approvedList = appService.getApplicationsByStatus("已批准");
        approved = approvedList != null ? approvedList.size() : 0;

        List<com.homestead.entity.Application> rejectedList = appService.getApplicationsByStatus("已驳回");
        rejected = rejectedList != null ? rejectedList.size() : 0;

        List<com.homestead.entity.Application> villageList = appService.getApplicationsByCurrentLevel("村级");
        List<com.homestead.entity.Application> townList = appService.getApplicationsByCurrentLevel("乡镇");
        int villagePending = villageList != null ? villageList.size() : 0;
        int townPending = townList != null ? townList.size() : 0;
        pending = villagePending + townPending;

        total = approved + rejected + pending;

        lblTotal.setText(String.valueOf(total));
        lblApproved.setText(String.valueOf(approved));
        lblPending.setText(String.valueOf(pending));
        lblRejected.setText(String.valueOf(rejected));

        // 月度数据（假设已实现 getMonthlyStatistics）
        monthlyData = appService.getMonthlyStatistics(6);
        if (monthlyData == null) monthlyData = java.util.Collections.emptyList();

        refreshCharts();
    }

    private void refreshCharts() {
        monthlyChartPanel.removeAll();
        pieChartPanel.removeAll();

        monthlyChartPanel.add(createMonthlyChart(), BorderLayout.CENTER);
        pieChartPanel.add(createPieChart(), BorderLayout.CENTER);

        monthlyChartPanel.revalidate();
        monthlyChartPanel.repaint();
        pieChartPanel.revalidate();
        pieChartPanel.repaint();
    }

    private JPanel createMonthlyChart() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (monthlyData.isEmpty()) {
                    g.drawString("暂无数据", getWidth() / 2 - 30, getHeight() / 2);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth() - 80;
                int height = getHeight() - 80;
                int barWidth = Math.min(50, width / monthlyData.size() - 10);
                int startX = 50;
                int baseY = getHeight() - 40;

                int maxTotal = monthlyData.stream().mapToInt(row -> ((Number) row[1]).intValue()).max().orElse(1);

                for (int i = 0; i < monthlyData.size(); i++) {
                    Object[] row = monthlyData.get(i);
                    String month = (String) row[0];
                    int totalVal = ((Number) row[1]).intValue();
                    int barHeight = (int) ((double) totalVal / maxTotal * (height - 40));
                    if (barHeight < 1) barHeight = 1;

                    g2.setColor(UIUtil.COLOR_PRIMARY);
                    g2.fillRect(startX + i * (barWidth + 10), baseY - barHeight, barWidth, barHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawString(month, startX + i * (barWidth + 10) + 5, baseY + 15);
                }
            }
        };
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 250));
        return panel;
    }

    private JPanel createPieChart() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (total == 0) {
                    g.drawString("暂无数据", getWidth() / 2 - 30, getHeight() / 2);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 60;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                double approvedAngle = 360.0 * approved / total;
                double pendingAngle = 360.0 * pending / total;
                double rejectedAngle = 360.0 * rejected / total;

                g2.setColor(UIUtil.COLOR_SUCCESS);
                g2.fillArc(x, y, size, size, 0, (int) approvedAngle);
                g2.setColor(UIUtil.COLOR_WARNING);
                g2.fillArc(x, y, size, size, (int) approvedAngle, (int) pendingAngle);
                g2.setColor(UIUtil.COLOR_DANGER);
                g2.fillArc(x, y, size, size, (int) (approvedAngle + pendingAngle), (int) rejectedAngle);

                // 图例
                int legendX = getWidth() - 80;
                int legendY = 30;
                g2.setColor(UIUtil.COLOR_SUCCESS);
                g2.fillRect(legendX, legendY, 15, 15);
                g2.setColor(Color.BLACK);
                g2.drawString("已通过", legendX + 20, legendY + 12);
                g2.setColor(UIUtil.COLOR_WARNING);
                g2.fillRect(legendX, legendY + 25, 15, 15);
                g2.drawString("审核中", legendX + 20, legendY + 37);
                g2.setColor(UIUtil.COLOR_DANGER);
                g2.fillRect(legendX, legendY + 50, 15, 15);
                g2.drawString("驳回", legendX + 20, legendY + 62);
            }
        };
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 250));
        return panel;
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存统计报表");
        fileChooser.setSelectedFile(new File("宅基地统计报表.csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV文件 (*.csv)", "csv"));
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) {
            path += ".csv";
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("汇总统计");
            writer.println("总申请数," + total);
            writer.println("已通过," + approved);
            writer.println("审核中," + pending);
            writer.println("驳回量," + rejected);

            if (monthlyData != null && !monthlyData.isEmpty()) {
                writer.println();
                writer.println("月度申请量趋势");
                writer.println("月份,申请总数,批准数,驳回数");
                for (Object[] row : monthlyData) {
                    writer.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
                }
            }

            JOptionPane.showMessageDialog(this, "导出成功！\n文件保存在：" + path, "导出完成", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "导出失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}