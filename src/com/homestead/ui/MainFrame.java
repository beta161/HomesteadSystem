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
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 左侧菜单
        JPanel leftMenuPanel = new JPanel();
        leftMenuPanel.setLayout(new BoxLayout(leftMenuPanel, BoxLayout.Y_AXIS));
        leftMenuPanel.setBackground(new Color(228, 243, 240));
        leftMenuPanel.setPreferredSize(new Dimension(200, 0));

        // 根据角色添加菜单项
        String role = loginUser.getRole();

        // 通用菜单（所有用户可见）
        if (!"管理员".equals(role)) { // 普通用户（申请人、审批人）
            addMenuItem(leftMenuPanel, "申请登记", "apply");
        }
        if (role.contains("村级") || role.contains("乡镇")) {
            addMenuItem(leftMenuPanel, "审批管理", "approve");
        }
        if (!"管理员".equals(role)) {
            addMenuItem(leftMenuPanel, "申诉处理", "appeal"); // 普通用户提交申诉
        }
        addMenuItem(leftMenuPanel, "附件管理", "attachment");

        // 管理员专属菜单
        if ("管理员".equals(role)) {
            addMenuItem(leftMenuPanel, "确权登记", "registration");
            addMenuItem(leftMenuPanel, "公示管理", "notice");
            addMenuItem(leftMenuPanel, "系统日志", "log");
            addMenuItem(leftMenuPanel, "申诉处理", "appealAdmin"); // 管理员处理申诉
        }

        leftMenuPanel.add(Box.createVerticalGlue());

        JButton btnExit = UIUtil.createButton("退出系统");
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setPreferredSize(new Dimension(160, 40));
        btnExit.addActionListener(e -> {
            int confirm = UIUtil.showConfirm("确定要退出系统吗？");
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        leftMenuPanel.add(btnExit);
        leftMenuPanel.add(Box.createVerticalStrut(20));

        // 右侧卡片区域
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtil.COLOR_WHITE);

        contentPanel.add(new ApplyFrame(loginUser), "apply");
        contentPanel.add(new ApproveFrame(loginUser), "approve");
        contentPanel.add(new AppealFrame(loginUser, false), "appeal");       // 普通用户申诉提交
        contentPanel.add(new AttachmentFrame(loginUser), "attachment");
        contentPanel.add(new LandRegistrationFrame(loginUser), "registration");
        contentPanel.add(new PublicNoticeFrame(loginUser), "notice");
        contentPanel.add(new SystemLogFrame(loginUser), "log");
        contentPanel.add(new AppealFrame(loginUser, true), "appealAdmin");   // 管理员申诉处理

        add(leftMenuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "apply");
    }

    private void addMenuItem(JPanel panel, String text, String cardName) {
        JButton btn = UIUtil.createButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(new Color(49, 216, 241));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(146, 226, 119));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(241, 218, 124));
            }
        });
        panel.add(Box.createVerticalStrut(8));
        panel.add(btn);
    }
}