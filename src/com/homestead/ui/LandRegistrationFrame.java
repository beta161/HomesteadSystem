package com.homestead.ui;

import com.homestead.entity.LandRegistration;
import com.homestead.entity.User;
import com.homestead.service.LandRegistrationService;
import com.homestead.service.impl.LandRegistrationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class LandRegistrationFrame extends JPanel {
    private User loginUser;
    private JTextField tfAppId;
    private JTextField tfCertNo;
    private LandRegistrationService regService;

    public LandRegistrationFrame(User user) {
        this.loginUser = user;
        this.regService = new LandRegistrationServiceImpl();
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(UIUtil.COLOR_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = UIUtil.createLabel("宅基地确权登记", true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblAppId, gbc);
        tfAppId = UIUtil.createTextField();
        tfAppId.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        add(tfAppId, gbc);

        JLabel lblCert = UIUtil.createLabel("确权证书号：", false);
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblCert, gbc);
        tfCertNo = UIUtil.createTextField();
        tfCertNo.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        add(tfCertNo, gbc);

        JButton btnSubmit = UIUtil.createButton("完成确权");
        btnSubmit.setPreferredSize(new Dimension(140, 40));
        btnSubmit.addActionListener(e -> submitReg());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(btnSubmit, gbc);
    }

    private void submitReg() {
        String appIdStr = tfAppId.getText().trim();
        String certNo = tfCertNo.getText().trim();

        if (appIdStr.isEmpty() || certNo.isEmpty()) {
            UIUtil.showError("申请ID和证书号不能为空！");
            return;
        }

        try {
            LandRegistration reg = new LandRegistration();
            reg.setAppId(Integer.parseInt(appIdStr));
            reg.setCertNo(certNo);
            reg.setRegTime(new Date());
            reg.setOperatorId(loginUser.getUserId());

            boolean success = regService.createLandRegistration(reg);
            if (success) {
                UIUtil.showInfo("确权登记成功！");
                clearForm();
            } else {
                UIUtil.showError("登记失败！申请ID可能已存在！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID必须为整数！");
        }
    }

    private void clearForm() {
        tfAppId.setText("");
        tfCertNo.setText("");
    }
}