package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyApplicationsFrame extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;
    private ApplicationService appService;

    public MyApplicationsFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        JLabel lblTitle = UIUtil.createLabel("我的申请", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        card.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"申请ID", "面积(㎡)", "用途", "当前状态", "当前审批环节", "提交时间"};
        model = new DefaultTableModel(columns, 0);
        table = UIUtil.createTable();
        table.setModel(model);
        // 设置状态列渲染（可选）
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = UIUtil.createButton("刷新");
        btnRefresh.addActionListener(e -> loadData());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnRefresh);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Application> list = appService.getApplicationsByUserId(user.getUserId());
        if (list != null && !list.isEmpty()) {
            for (Application app : list) {
                String timeStr = app.getApplyTime() != null ? app.getApplyTime().toLocaleString() : "";
                model.addRow(new Object[]{
                        app.getAppId(),
                        app.getPlotArea(),
                        app.getPurpose(),
                        app.getStatus(),
                        app.getCurrentApprovalLevel() == null ? "-" : app.getCurrentApprovalLevel(),
                        timeStr
                });
            }
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