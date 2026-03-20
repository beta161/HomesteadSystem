package com.homestead.ui;

import com.homestead.entity.Appeal;
import com.homestead.entity.User;
import com.homestead.service.AppealService;
import com.homestead.service.impl.AppealServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class AppealFrame extends JPanel {
    private User user;
    private JTextField tfAppId;
    private JTextArea taReason;
    private JTable table;
    private DefaultTableModel model;
    private AppealService appealService;

    public AppealFrame(User user) {
        this.user = user;
        this.appealService = new AppealServiceImpl();
        initUI();
//        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 提交面板
        JPanel panel = new JPanel(new GridBagLayout());
    }
}