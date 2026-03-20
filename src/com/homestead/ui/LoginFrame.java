package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.service.UserService;
import com.homestead.service.impl.UserServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private UserService userService;

    public LoginFrame() {
        userService = new UserServiceImpl();
        initUI();
    }

    private void initUI() {
        setTitle("宅基地管理系统 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setBackground(UIUtil.COLOR_LIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitle = UIUtil.createLabel("宅基地管理系统", true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblUser = UIUtil.createLabel("用户名：", false);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblUser, gbc);

        tfUsername = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 1;
        add(tfUsername, gbc);

        JLabel lblPwd = UIUtil.createLabel("密码：", false);
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPwd, gbc);

        pfPassword = new JPasswordField();
        pfPassword.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1; gbc.gridy = 2;
        add(pfPassword, gbc);

        JButton btnLogin = UIUtil.createButton("登录");
        btnLogin.addActionListener(e -> doLogin());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnLogin, gbc);
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
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}