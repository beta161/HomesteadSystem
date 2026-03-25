package com.homestead.ui;

import com.homestead.entity.SystemLog;
import com.homestead.entity.User;
import com.homestead.service.SystemLogService;
import com.homestead.service.impl.SystemLogServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SystemLogFrame extends JPanel {
    private User loginUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private SystemLogService logService;

    public SystemLogFrame(User user) {
        this.loginUser = user;
        this.logService = new SystemLogServiceImpl();
        initUI();
        loadLogs();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new BorderLayout());
        card.setLayout(new BorderLayout());

        JLabel lblTitle = UIUtil.createLabel("系统操作日志", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        card.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"日志ID", "操作人ID", "操作类型", "操作详情", "操作时间"};
        tableModel = new DefaultTableModel(columns, 0);
        table = UIUtil.createTable();
        table.setModel(tableModel);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = UIUtil.createButton("刷新日志");
        btnRefresh.addActionListener(e -> loadLogs());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnRefresh);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        List<SystemLog> list;
        if ("管理员".equals(loginUser.getRole())) {
            list = logService.getAllLogs();
        } else {
            list = logService.getLogsByUserId(loginUser.getUserId());
        }
        if (list != null) {
            for (SystemLog log : list) {
                String timeStr = log.getOpTime() != null ? log.getOpTime().toLocaleString() : "";
                tableModel.addRow(new Object[]{
                        log.getLogId(),
                        log.getUserId(),
                        log.getOperation(),
                        log.getDetail(),
                        timeStr
                });
            }
        }
    }
}