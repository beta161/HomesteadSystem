package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.ApprovalRecord;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.ApprovalRecordService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.service.impl.ApprovalRecordServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ApproveFrame extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;
    private ApplicationService appService;
    private ApprovalRecordService recordService;

    public ApproveFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        this.recordService = new ApprovalRecordServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_WHITE);

        JLabel lblTitle = UIUtil.createLabel("待审批申请列表", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"申请ID", "申请人ID", "面积", "用途", "当前状态", "审批环节"}, 0);
        table = UIUtil.createTable();
        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRefresh = UIUtil.createButton("刷新");
        btnRefresh.addActionListener(e -> loadData());
        JButton btnApprove = UIUtil.createButton("通过");
        btnApprove.addActionListener(e -> approve(true));
        JButton btnReject = UIUtil.createButton("驳回");
        btnReject.addActionListener(e -> approve(false));
        btnPanel.add(btnRefresh);
        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        add(btnPanel, BorderLayout.SOUTH);
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
        if (list != null) {
            for (Application app : list) {
                model.addRow(new Object[]{
                        app.getAppId(),
                        app.getUserId(),
                        app.getPlotArea(),
                        app.getPurpose(),
                        app.getStatus(),
                        app.getCurrentApprovalLevel()
                });
            }
        }
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

        // 输入审批意见
        String opinion = JOptionPane.showInputDialog(this, "请输入审批意见：", "审批", JOptionPane.PLAIN_MESSAGE);
        if (opinion == null) return; // 取消

        // 记录审批记录
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

        // 更新申请状态和环节
        String newStatus, newLevel;
        if (pass) {
            if ("村级".equals(app.getCurrentApprovalLevel())) {
                newStatus = "待乡镇审批";
                newLevel = "乡镇";
            } else { // 乡镇审批通过
                newStatus = "已批准";
                newLevel = "";
            }
        } else {
            newStatus = "已驳回";
            newLevel = "";
        }
        boolean updateSuccess = appService.updateAppStatusAndLevel(appId, newStatus, newLevel);
        if (updateSuccess) {
            UIUtil.showInfo("审批成功！");
            loadData();
        } else {
            UIUtil.showError("审批失败！");
        }
    }
}