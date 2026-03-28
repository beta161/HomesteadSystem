package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.service.UserService;
import com.homestead.service.impl.UserServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private UserService userService;

    public LoginFrame() {
        userService = new UserServiceImpl();
        initUI();
    }

    private void initUI() {
        setTitle("宅基地管理系统");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 左侧品牌区（渐变）
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIUtil.COLOR_PRIMARY_DARK, 0, getHeight(), UIUtil.COLOR_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(450, 0));
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(20, 30, 20, 30);
        gbcLeft.anchor = GridBagConstraints.CENTER;

        JLabel lblLogo = new JLabel("🏠");
        lblLogo.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 64));
        lblLogo.setForeground(Color.WHITE);
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        leftPanel.add(lblLogo, gbcLeft);

        JLabel lblTitle = new JLabel("全国农村宅基地管理系统");
        lblTitle.setFont(new Font("微软雅黑", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        gbcLeft.gridy = 1;
        leftPanel.add(lblTitle, gbcLeft);

        JLabel lblDesc = new JLabel("<html><center>一站式申请 · 多级审批 · 全程监管<br>阳光公开 · 便民利民</center></html>");
        lblDesc.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(240, 240, 240));
        gbcLeft.gridy = 2;
        leftPanel.add(lblDesc, gbcLeft);

        // 右侧登录表单（卡片）
        JPanel rightPanel = UIUtil.createCardPanel(new GridBagLayout());
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(15, 30, 15, 30);
        gbcRight.anchor = GridBagConstraints.CENTER;

        JLabel lblWelcome = new JLabel("欢迎登录");
        lblWelcome.setFont(UIUtil.FONT_TITLE);
        lblWelcome.setForeground(UIUtil.COLOR_PRIMARY);
        gbcRight.gridx = 0; gbcRight.gridy = 0; gbcRight.gridwidth = 2;
        rightPanel.add(lblWelcome, gbcRight);

        // 用户名
        JLabel lblUser = UIUtil.createLabel("用户名", false);
        gbcRight.gridy = 1; gbcRight.gridwidth = 1;
        gbcRight.anchor = GridBagConstraints.WEST;
        rightPanel.add(lblUser, gbcRight);

        tfUsername = UIUtil.createTextField();
        tfUsername.setPreferredSize(new Dimension(260, 40));
        tfUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tfUsername.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_PRIMARY, 2, true));
            }
            @Override
            public void focusLost(FocusEvent e) {
                tfUsername.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true));
            }
        });
        gbcRight.gridy = 2;
        rightPanel.add(tfUsername, gbcRight);

        // 密码
        JLabel lblPwd = UIUtil.createLabel("密码", false);
        gbcRight.gridy = 3;
        rightPanel.add(lblPwd, gbcRight);

        pfPassword = new JPasswordField();
        pfPassword.setFont(UIUtil.FONT_BODY);
        pfPassword.setPreferredSize(new Dimension(260, 40));
        pfPassword.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true));
        pfPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                pfPassword.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_PRIMARY, 2, true));
            }
            @Override
            public void focusLost(FocusEvent e) {
                pfPassword.setBorder(BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true));
            }
        });
        gbcRight.gridy = 4;
        rightPanel.add(pfPassword, gbcRight);

        // 登录按钮
        JButton btnLogin = UIUtil.createButton("登录");
        btnLogin.setPreferredSize(new Dimension(260, 42));
        btnLogin.addActionListener(e -> doLogin());
        gbcRight.gridy = 5; gbcRight.gridwidth = 2; gbcRight.anchor = GridBagConstraints.CENTER;
        rightPanel.add(btnLogin, gbcRight);

        // 注册按钮
        JButton btnRegister = new JButton("注册账号");
        btnRegister.setFont(UIUtil.FONT_SMALL);
        btnRegister.setForeground(UIUtil.COLOR_PRIMARY);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> new RegisterFrame().setVisible(true));
        gbcRight.gridy = 6;  // 下一行
        rightPanel.add(btnRegister, gbcRight);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void doLogin() {
        String username = tfUsername.getText().trim();
        String pwd = new String(pfPassword.getPassword()).trim();
        if (username.isEmpty() || pwd.isEmpty()) {
            UIUtil.showError("账号密码不能为空！");
            return;
        }
        User user = userService.login(username, pwd);
        if (user != null) {
            new MainFrame(user).setVisible(true);
            this.dispose();
        } else {
            UIUtil.showError("账号或密码错误！");
        }
    }

    public static void main(String[] args) {
        UIUtil.enableAntiAliasing(); // 开启抗锯齿
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}