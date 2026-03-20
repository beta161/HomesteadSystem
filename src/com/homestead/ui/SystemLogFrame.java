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
        setBackground(UIUtil.COLOR_WHITE);

        JLabel lblTitle = UIUtil.createLabel("系统操作日志", true);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(lblTitle, BorderLayout.NORTH);

        // 表格
        String[] columns = {"日志ID", "操作人ID", "操作类型", "操作详情", "操作时间"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(UIUtil.FONT_SMALL);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 按钮
        JButton btnRefresh = UIUtil.createButton("刷新日志");
        btnRefresh.addActionListener(e -> loadLogs());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * 加载日志数据
     */
    private void loadLogs() {
        tableModel.setRowCount(0);
        List<SystemLog> list;

        // 管理员查看所有，普通用户查看自己
        if (loginUser.getRole().contains("管理员")) {
            list = logService.getAllLogs();
        } else {
            list = logService.getLogsByUserId(loginUser.getUserId());
        }

        if (list == null || list.isEmpty()) {
            tableModel.addRow(new Object[]{"暂无日志记录", "", "", "", ""});
            return;
        }

        for (SystemLog log : list) {
            Object[] row = new Object[5];
            row[0] = log.getLogId();
            row[1] = log.getUserId();
            row[2] = log.getOperation();
            row[3] = log.getDetail();
            row[4] = log.getOpTime().toLocaleString();
            tableModel.addRow(row);
        }
    }
}