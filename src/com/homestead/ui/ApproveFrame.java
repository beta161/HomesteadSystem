package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.ApprovalRecord;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.ApprovalRecordService;
import com.homestead.service.ApprovalTimerService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.service.impl.ApprovalRecordServiceImpl;
import com.homestead.service.impl.ApprovalTimerServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApproveFrame extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;
    private ApplicationService appService;
    private ApprovalRecordService recordService;
    private ApprovalTimerService timerService;
    private JPanel stepPanel;

    // 分页和搜索相关
    private JTextField tfKeyword;
    private JButton btnSearch;
    private JButton btnFirst, btnPrev, btnNext, btnLast;
    private JLabel lblPageInfo;
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;
    private String currentKeyword = "";

    public ApproveFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        this.recordService = new ApprovalRecordServiceImpl();
        this.timerService = new ApprovalTimerServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        // ==================== 顶部区域（搜索栏 + 步骤条）====================
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBackground(Color.WHITE);

        // ----- 搜索和分页栏 -----
        JPanel searchPaginationPanel = new JPanel(new BorderLayout());
        searchPaginationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchPaginationPanel.setBackground(Color.WHITE);

        // 左侧：搜索框 + 搜索按钮
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("关键字："));
        tfKeyword = UIUtil.createTextField();
        tfKeyword.setPreferredSize(new Dimension(180, 30));
        searchPanel.add(tfKeyword);
        btnSearch = UIUtil.createButton("搜索");
        btnSearch.addActionListener(e -> {
            currentPage = 1;
            currentKeyword = tfKeyword.getText().trim();
            loadData();
        });
        searchPanel.add(btnSearch);
        searchPaginationPanel.add(searchPanel, BorderLayout.WEST);

        // 右侧：分页控件
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        paginationPanel.setBackground(Color.WHITE);
        btnFirst = UIUtil.createButton("首页");
        btnPrev = UIUtil.createButton("上一页");
        lblPageInfo = new JLabel("第1页 / 共1页");
        lblPageInfo.setFont(UIUtil.FONT_SMALL);
        btnNext = UIUtil.createButton("下一页");
        btnLast = UIUtil.createButton("末页");

        btnFirst.addActionListener(e -> goToPage(1));
        btnPrev.addActionListener(e -> goToPage(currentPage - 1));
        btnNext.addActionListener(e -> goToPage(currentPage + 1));
        btnLast.addActionListener(e -> goToPage(totalPages));

        paginationPanel.add(btnFirst);
        paginationPanel.add(btnPrev);
        paginationPanel.add(lblPageInfo);
        paginationPanel.add(btnNext);
        paginationPanel.add(btnLast);
        searchPaginationPanel.add(paginationPanel, BorderLayout.EAST);

        northPanel.add(searchPaginationPanel);

        // ----- 步骤条 -----
        stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        stepPanel.setBackground(Color.WHITE);
        stepPanel.setBorder(BorderFactory.createTitledBorder("审批流程"));
        northPanel.add(stepPanel);

        card.add(northPanel, BorderLayout.NORTH);

        // ==================== 表格 ====================
        model = new DefaultTableModel(new Object[]{"申请ID", "申请人ID", "面积(㎡)", "用途", "当前状态", "审批环节", "剩余时限"}, 0);
        table = UIUtil.createTable();
        table.setModel(model);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // ==================== 底部按钮 ====================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRefresh = UIUtil.createButton("刷新");
        btnRefresh.addActionListener(e -> {
            currentPage = 1;
            currentKeyword = tfKeyword.getText().trim();
            loadData();
        });
        JButton btnApprove = UIUtil.createButton("通过");
        btnApprove.addActionListener(e -> approve(true));
        JButton btnReject = UIUtil.createButton("驳回");
        btnReject.addActionListener(e -> approve(false));
        btnPanel.add(btnRefresh);
        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    /**
     * 跳转到指定页
     */
    private void goToPage(int page) {
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        if (page == currentPage) return;
        currentPage = page;
        loadData();
    }

    /**
     * 加载当前页数据（带分页和搜索）
     */
    private void loadData() {
        model.setRowCount(0);
        String role = user.getRole();
        String level = null;
        if (role.contains("村级")) {
            level = "村级";
        } else if (role.contains("乡镇")) {
            level = "乡镇";
        }

        // 查询总记录数
        int totalRecords = appService.countApplicationsByCurrentLevel(level, currentKeyword);
        if (totalRecords == 0) {
            stepPanel.removeAll();
            stepPanel.add(new JLabel("暂无待审批申请"));
            totalPages = 1;
            updatePaginationState();
            return;
        }

        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (currentPage > totalPages) currentPage = totalPages;

        // 查询当前页数据
        List<Application> list = appService.getApplicationsByCurrentLevelWithPage(level, currentKeyword, currentPage, pageSize);

        if (list != null && !list.isEmpty()) {
            updateStepPanel(list.get(0).getCurrentApprovalLevel());
            for (Application app : list) {
                String remaining = calculateRemainingTime(app);
                model.addRow(new Object[]{
                        app.getAppId(),
                        app.getUserId(),
                        app.getPlotArea(),
                        app.getPurpose(),
                        app.getStatus(),
                        app.getCurrentApprovalLevel(),
                        remaining
                });
            }
        } else {
            stepPanel.removeAll();
            stepPanel.add(new JLabel("暂无待审批申请"));
        }

        updatePaginationState();
        revalidate();
        repaint();
    }

    /**
     * 更新分页按钮状态
     */
    private void updatePaginationState() {
        lblPageInfo.setText("第" + currentPage + "页 / 共" + totalPages + "页");
        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);
    }

    private void updateStepPanel(String currentLevel) {
        stepPanel.removeAll();
        String[] steps = {"提交申请", "村级审批", "乡镇审批", "批准/驳回"};
        String[] statuses = new String[4];
        if (currentLevel == null || currentLevel.isEmpty()) {
            statuses[0] = "completed"; statuses[1] = "completed"; statuses[2] = "completed"; statuses[3] = "active";
        } else if ("村级".equals(currentLevel)) {
            statuses[0] = "completed"; statuses[1] = "active"; statuses[2] = "pending"; statuses[3] = "pending";
        } else if ("乡镇".equals(currentLevel)) {
            statuses[0] = "completed"; statuses[1] = "completed"; statuses[2] = "active"; statuses[3] = "pending";
        } else {
            statuses[0] = "completed"; statuses[1] = "completed"; statuses[2] = "completed"; statuses[3] = "completed";
        }

        for (int i = 0; i < steps.length; i++) {
            JPanel step = new JPanel(new BorderLayout());
            step.setOpaque(false);
            JLabel lblStep = new JLabel(steps[i]);
            lblStep.setFont(UIUtil.FONT_SMALL);
            JLabel lblStatus = new JLabel();
            switch (statuses[i]) {
                case "completed": lblStatus.setText("✓"); lblStatus.setForeground(UIUtil.COLOR_SUCCESS); break;
                case "active": lblStatus.setText("●"); lblStatus.setForeground(UIUtil.COLOR_PRIMARY); break;
                default: lblStatus.setText("○"); lblStatus.setForeground(UIUtil.COLOR_TEXT_HINT);
            }
            step.add(lblStatus, BorderLayout.NORTH);
            step.add(lblStep, BorderLayout.SOUTH);
            stepPanel.add(step);
            if (i < steps.length - 1) stepPanel.add(new JLabel(" → "));
        }
        stepPanel.revalidate();
        stepPanel.repaint();
    }

    private String calculateRemainingTime(Application app) {
        // 简化模拟，实际应从 ApprovalTimers 表计算
        if ("待村级审批".equals(app.getStatus())) return "剩余 5 天";
        if ("待乡镇审批".equals(app.getStatus())) return "剩余 3 天";
        return "-";
    }

    private void approve(boolean pass) {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtil.showError("请先选择一条申请！");
            return;
        }
        Integer appId = (Integer) model.getValueAt(row, 0);
        Application app = appService.getApplicationById(appId);
        if (app == null) {
            UIUtil.showError("申请不存在！");
            return;
        }

        String opinion = JOptionPane.showInputDialog(this, "请输入审批意见：", "审批", JOptionPane.PLAIN_MESSAGE);
        if (opinion == null) return;

        ApprovalRecord record = new ApprovalRecord();
        record.setAppId(appId);
        record.setApproverId(user.getUserId());
        record.setLevel(app.getCurrentApprovalLevel());
        record.setOpinion(opinion);
        record.setResult(pass ? "通过" : "驳回");
        boolean recordSuccess = recordService.addApprovalRecord(record);
        if (!recordSuccess) {
            UIUtil.showError("记录审批失败，操作中止！");
            return;
        }

        String newStatus, newLevel;
        if (pass) {
            if ("村级".equals(app.getCurrentApprovalLevel())) {
                newStatus = "待乡镇审批";
                newLevel = "乡镇";
                // 村级审批通过后，初始化乡镇审批时限（15天）
                boolean timerInit = timerService.initApprovalTimer(appId, "乡镇", 15);
                if (!timerInit) {
                    System.err.println("乡镇审批时限初始化失败，申请ID = " + appId);
                } else {
                    System.out.println("乡镇审批时限初始化成功，申请ID = " + appId);
                }
            } else {
                newStatus = "已批准";
                newLevel = null;
            }
        } else {
            newStatus = "已驳回";
            newLevel = null;
        }

        boolean updateSuccess = appService.updateAppStatusAndLevel(appId, newStatus, newLevel);
        if (updateSuccess) {
            UIUtil.showInfo("审批成功！");
            loadData();
        } else {
            UIUtil.showError("审批失败！");
        }
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;
            if (status != null) {
                switch (status) {
                    case "待村级审批":
                    case "待乡镇审批":
                        c.setBackground(UIUtil.COLOR_WARNING);
                        break;
                    case "已批准":
                        c.setBackground(UIUtil.COLOR_SUCCESS);
                        break;
                    case "已驳回":
                        c.setBackground(UIUtil.COLOR_DANGER);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
}