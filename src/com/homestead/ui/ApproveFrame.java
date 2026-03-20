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

public class ApproveFrame extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;
    private ApplicationService appService;

    public ApproveFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
//        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID", "申请人ID", "面积", "用途", "状态"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = UIUtil.createButton("刷新");
//        btnRefresh.addActionListener(e -> loadData());
        add(btnRefresh, BorderLayout.SOUTH);
    }

//    private void loadData() {
//        model.setRowCount(0);
//        List<Application> list = appService.getApplicationsByStatus("待审批");
//        for (Application app : list) {
//            model.addRow(new Object[]{app.getAppId(), app.getAppId(), app.getPlotArea(), app.getPurpose(), app.getStatus()});
//        }
//    }
}