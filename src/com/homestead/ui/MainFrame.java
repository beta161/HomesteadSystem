package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User loginUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame(User user) {
        this.loginUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("宅基地管理系统 - 当前用户：" + loginUser.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===================== 左侧菜单 =====================
        JPanel leftMenuPanel = new JPanel();
        leftMenuPanel.setLayout(new BoxLayout(leftMenuPanel, BoxLayout.Y_AXIS));
        leftMenuPanel.setBackground(UIUtil.COLOR_MAIN);
        leftMenuPanel.setPreferredSize(new Dimension(180, 0));

        // 通用菜单（所有用户可见）
        addMenuItem(leftMenuPanel, "申请登记", "apply");
        addMenuItem(leftMenuPanel, "审批管理", "approve");
        addMenuItem(leftMenuPanel, "申诉处理", "appeal");
        addMenuItem(leftMenuPanel, "附件管理", "attachment");

        // 管理员菜单
        if (loginUser.getRole().equals("管理员")) {
            addMenuItem(leftMenuPanel, "确权登记", "registration");
            addMenuItem(leftMenuPanel, "公示管理", "notice");
            addMenuItem(leftMenuPanel, "系统日志", "log");
        }

        // 退出按钮
        JButton btnExit = UIUtil.createButton("退出系统");
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setPreferredSize(new Dimension(150, 40));
        btnExit.addActionListener(e -> {
            int confirm = UIUtil.showConfirm("确定要退出系统吗？");
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        leftMenuPanel.add(Box.createVerticalGlue());
        leftMenuPanel.add(btnExit);
        leftMenuPanel.add(Box.createVerticalStrut(20));

        // ===================== 右侧卡片布局面板 =====================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 把所有界面全部加进来！
        contentPanel.add(new ApplyFrame(loginUser), "apply");
        contentPanel.add(new ApproveFrame(loginUser), "approve");
        contentPanel.add(new AppealFrame(loginUser), "appeal");
        contentPanel.add(new AttachmentFrame(loginUser), "attachment");
        contentPanel.add(new LandRegistrationFrame(loginUser), "registration");
        contentPanel.add(new PublicNoticeFrame(loginUser), "notice");
        contentPanel.add(new SystemLogFrame(loginUser), "log");

        // ===================== 组装 =====================
        add(leftMenuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // 默认显示申请界面
        cardLayout.show(contentPanel, "apply");
    }

    // 封装菜单按钮
    private void addMenuItem(JPanel panel, String text, String cardName) {
        JButton btn = UIUtil.createButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));

        panel.add(Box.createVerticalStrut(10));
        panel.add(btn);
    }
}