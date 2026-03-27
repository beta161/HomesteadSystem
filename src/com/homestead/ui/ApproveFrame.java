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

        stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        stepPanel.setBackground(Color.WHITE);
        stepPanel.setBorder(BorderFactory.createTitledBorder("审批流程"));
        card.add(stepPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"申请ID", "申请人ID", "面积(㎡)", "用途", "当前状态", "审批环节", "剩余时限"}, 0);
        table = UIUtil.createTable();
        table.setModel(model);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRefresh = UIUtil.createButton("刷新");
        btnRefresh.addActionListener(e -> loadData());
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

    private void loadData() {
        model.setRowCount(0);
        String role = user.getRole();
        List<Application> list;
        if (role.contains("村级")) {
            list = appService.getApplicationsByCurrentLevel("村级");
        } else if (role.contains("乡镇")) {
            list = appService.getApplicationsByCurrentLevel("乡镇");
        } else {
            list = null;
        }

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
        revalidate();
        repaint();
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