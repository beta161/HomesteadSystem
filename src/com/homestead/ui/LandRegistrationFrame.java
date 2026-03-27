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
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = UIUtil.createCardPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = UIUtil.createLabel("宅基地确权登记", true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(lblTitle, gbc);

        JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        card.add(lblAppId, gbc);
        tfAppId = UIUtil.createTextField();
        tfAppId.setPreferredSize(new Dimension(280, 38));
        gbc.gridx = 1;
        card.add(tfAppId, gbc);

        JLabel lblCert = UIUtil.createLabel("确权证书号：", false);
        gbc.gridx = 0; gbc.gridy = 2;
        card.add(lblCert, gbc);
        tfCertNo = UIUtil.createTextField();
        tfCertNo.setPreferredSize(new Dimension(280, 38));
        gbc.gridx = 1;
        card.add(tfCertNo, gbc);

        JButton btnSubmit = UIUtil.createButton("完成确权");
        btnSubmit.setPreferredSize(new Dimension(160, 42));
        btnSubmit.addActionListener(e -> submitReg());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnSubmit, gbc);

        add(card);
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
                UIUtil.showError("登记失败！申请ID可能已存在或无效！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID必须为整数！");
        } catch (Exception e) {
            UIUtil.showError("操作失败：" + e.getMessage());
        }
    }

    private void clearForm() {
        tfAppId.setText("");
        tfCertNo.setText("");
    }
}