package com.homestead.ui;

import com.homestead.entity.User;
import com.homestead.service.UserService;
import com.homestead.service.impl.UserServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegisterFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirm;
    private JTextField tfPhone;
    private UserService userService;

    public RegisterFrame() {
        userService = new UserServiceImpl();
        initUI();
    }

    private void initUI() {
        setTitle("用户注册");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel card = UIUtil.createCardPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitle = UIUtil.createLabel("新用户注册", true);
        lblTitle.setForeground(UIUtil.COLOR_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(lblTitle, gbc);

        // 用户名
        JLabel lblUser = UIUtil.createLabel("用户名：", false);
        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(lblUser, gbc);
        tfUsername = UIUtil.createTextField();
        tfUsername.setPreferredSize(new Dimension(260, 35));
        gbc.gridx = 1;
        card.add(tfUsername, gbc);

        // 密码
        JLabel lblPwd = UIUtil.createLabel("密码：", false);
        gbc.gridx = 0; gbc.gridy = 2;
        card.add(lblPwd, gbc);
        pfPassword = new JPasswordField();
        pfPassword.setFont(UIUtil.FONT_BODY);
        pfPassword.setPreferredSize(new Dimension(260, 35));
        pfPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        card.add(pfPassword, gbc);

        // 确认密码
        JLabel lblConfirm = UIUtil.createLabel("确认密码：", false);
        gbc.gridx = 0; gbc.gridy = 3;
        card.add(lblConfirm, gbc);
        pfConfirm = new JPasswordField();
        pfConfirm.setFont(UIUtil.FONT_BODY);
        pfConfirm.setPreferredSize(new Dimension(260, 35));
        pfConfirm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        card.add(pfConfirm, gbc);

        // 手机号
        JLabel lblPhone = UIUtil.createLabel("手机号：", false);
        gbc.gridx = 0; gbc.gridy = 4;
        card.add(lblPhone, gbc);
        tfPhone = UIUtil.createTextField();
        tfPhone.setPreferredSize(new Dimension(260, 35));
        gbc.gridx = 1;
        card.add(tfPhone, gbc);

        // 注册按钮
        JButton btnRegister = UIUtil.createButton("注册");
        btnRegister.setPreferredSize(new Dimension(120, 40));
        btnRegister.addActionListener(e -> doRegister());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnRegister, gbc);

        add(card);
    }

    private void doRegister() {
        String username = tfUsername.getText().trim();
        String pwd = new String(pfPassword.getPassword()).trim();
        String confirm = new String(pfConfirm.getPassword()).trim();
        String phone = tfPhone.getText().trim();

        if (username.isEmpty() || pwd.isEmpty() || confirm.isEmpty()) {
            UIUtil.showError("用户名、密码不能为空！");
            return;
        }
        if (!pwd.equals(confirm)) {
            UIUtil.showError("两次输入的密码不一致！");
            return;
        }
        if (pwd.length() < 6) {
            UIUtil.showError("密码长度至少6位！");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        user.setRole("申请人");
        user.setPhone(phone);

        if (userService.register(user)) {
            UIUtil.showInfo("注册成功！请登录。");
            dispose();
        } else {
            UIUtil.showError("注册失败，用户名可能已存在！");
        }
    }
}