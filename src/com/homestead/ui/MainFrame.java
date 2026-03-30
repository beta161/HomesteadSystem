package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class MainFrame extends JFrame {
    private User loginUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private ApplicationService appService;
    private ApplyFrame applyFrame;

    public MainFrame(User user) {
        this.loginUser = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
        checkTodoReminders();
    }

    private void initUI() {
        setIconImage(loadIcon());
        setTitle("全国农村宅基地管理系统");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 顶部渐变导航栏
        JPanel topBar = UIUtil.createGradientHeader();

        // 左侧部分：图标 + 系统名称
        // 左侧部分：图标 + 系统名称，使用 GridBagLayout 实现垂直居中
        JPanel leftTop = new JPanel(new GridBagLayout());
        leftTop.setOpaque(false);

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(0, 5, 0, 5);
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.fill = GridBagConstraints.VERTICAL; // 确保垂直填充

        // 小房子图标
        JLabel lblIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/icon/home.png"));
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblIcon.setText("🏠");
            lblIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
            lblIcon.setForeground(Color.WHITE);
        }
        leftTop.add(lblIcon, gbcLeft);

        // 系统名称
        JLabel lblSystem = new JLabel("全国农村宅基地管理系统");
        lblSystem.setFont(new Font("微软雅黑", Font.BOLD, 20));
        lblSystem.setForeground(Color.WHITE);
        gbcLeft.gridx = 1;
        leftTop.add(lblSystem, gbcLeft);

        topBar.add(leftTop, BorderLayout.WEST);

        // 右侧用户信息
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("欢迎，" + loginUser.getUsername() + " (" + loginUser.getRole() + ") [ID:" + loginUser.getUserId() + "]");
        lblUser.setFont(UIUtil.FONT_BODY);
        lblUser.setForeground(Color.WHITE);
        JButton btnLogout = UIUtil.createButton("退出");
        btnLogout.setBackground(UIUtil.COLOR_DANGER);
        btnLogout.addActionListener(e -> {
            int confirm = UIUtil.showConfirm("确定要退出系统吗？");
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        userPanel.add(lblUser);
        userPanel.add(btnLogout);
        topBar.add(userPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // 左侧菜单
        JPanel leftMenuPanel = new JPanel();
        leftMenuPanel.setLayout(new BoxLayout(leftMenuPanel, BoxLayout.Y_AXIS));
        leftMenuPanel.setBackground(UIUtil.COLOR_BG);
        leftMenuPanel.setPreferredSize(new Dimension(240, 0));
        leftMenuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIUtil.COLOR_BORDER));

        String role = loginUser.getRole();

        if ("申请人".equals(role)) {
            addMenuGroup(leftMenuPanel, "宅基地申请", new String[][]{{"申请登记", "apply"}, {"附件管理", "attachment"}});
            addMenuGroup(leftMenuPanel, "我的申请", new String[][]{{"查看申请", "myApps"}});
            addMenuGroup(leftMenuPanel, "申诉反馈", new String[][]{{"申诉处理", "appeal"}});
        } else if (role.contains("村级") || role.contains("乡镇")) {
            addMenuGroup(leftMenuPanel, "审批工作台", new String[][]{{"审批管理", "approve"}});
        } else if ("管理员".equals(role)) {
            addMenuGroup(leftMenuPanel, "行政管理", new String[][]{{"确权登记", "registration"}, {"公示管理", "notice"}, {"系统日志", "log"}});
            addMenuGroup(leftMenuPanel, "申诉处理", new String[][]{{"申诉处理", "appealAdmin"}});
            addMenuGroup(leftMenuPanel, "统计分析", new String[][]{{"统计报表", "stats"}});
        }

        leftMenuPanel.add(Box.createVerticalGlue());
        JButton btnExit = UIUtil.createButton("退出系统");
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setMaximumSize(new Dimension(200, 40));
        btnExit.setBackground(UIUtil.COLOR_DANGER);
        btnExit.addActionListener(e -> System.exit(0));
        leftMenuPanel.add(Box.createVerticalStrut(20));
        leftMenuPanel.add(btnExit);
        leftMenuPanel.add(Box.createVerticalStrut(20));

        add(leftMenuPanel, BorderLayout.WEST);

        // 右侧卡片内容区
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtil.COLOR_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        applyFrame = new ApplyFrame(loginUser);
        applyFrame.setName("apply");
        contentPanel.add(applyFrame, "apply");

        contentPanel.add(new ApproveFrame(loginUser), "approve");
        contentPanel.add(new MyApplicationsFrame(loginUser), "myApps");
        contentPanel.add(new AppealFrame(loginUser, false), "appeal");
        contentPanel.add(new AttachmentFrame(loginUser), "attachment");
        contentPanel.add(new LandRegistrationFrame(loginUser), "registration");
        contentPanel.add(new PublicNoticeFrame(loginUser), "notice");
        contentPanel.add(new SystemLogFrame(loginUser), "log");
        contentPanel.add(new AppealFrame(loginUser, true), "appealAdmin");
        contentPanel.add(new StatisticsFrame(loginUser), "stats");

        if (role.contains("村级") || role.contains("乡镇")) {
            cardLayout.show(contentPanel, "approve");
        } else {
            cardLayout.show(contentPanel, "apply");
            SwingUtilities.invokeLater(() -> applyFrame.requestFocusForInput());
        }

        add(contentPanel, BorderLayout.CENTER);
    }

    private void addMenuGroup(JPanel panel, String groupTitle, String[][] items) {
        JLabel lblGroup = new JLabel(groupTitle);
        lblGroup.setFont(UIUtil.FONT_SUBTITLE);
        lblGroup.setForeground(UIUtil.COLOR_PRIMARY);
        lblGroup.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        lblGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblGroup);

        for (String[] item : items) {
            JButton btn = new JButton(item[0]);
            btn.setFont(UIUtil.FONT_BODY);
            btn.setForeground(UIUtil.COLOR_TEXT_BODY);
            btn.setBackground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.addActionListener(e -> {
                cardLayout.show(contentPanel, item[1]);
                contentPanel.revalidate();
                contentPanel.repaint();
                if ("apply".equals(item[1]) && applyFrame != null) {
                    SwingUtilities.invokeLater(() -> applyFrame.requestFocusForInput());
                }
            });
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(230, 240, 255));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Color.WHITE);
                }
            });
            panel.add(btn);
            panel.add(Box.createVerticalStrut(5));
        }
    }

    private Image loadIcon() {
        URL iconURL = getClass().getResource("/images/icon/favicon.ico");
        if (iconURL != null) {
            return Toolkit.getDefaultToolkit().getImage(iconURL);
        }
        return null;
    }

    private void checkTodoReminders() {
        String role = loginUser.getRole();
        if (role.contains("村级") || role.contains("乡镇")) {
            String level = role.contains("村级") ? "村级" : "乡镇";
            List<Application> list = appService.getApplicationsByCurrentLevel(level);
            int pendingCount = (list != null) ? list.size() : 0;
            if (pendingCount > 0) {
                JOptionPane.showMessageDialog(this,
                        "您有 " + pendingCount + " 项待审批申请，请及时处理！",
                        "待办提醒",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}