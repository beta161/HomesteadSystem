package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ApplyFrame extends JPanel {
    private User user;
    private JTextField tfArea;
    private JTextField tfPurpose;
    private ApplicationService appService;

    public ApplyFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // 申请人信息组
        JPanel infoGroup = new JPanel(new GridBagLayout());
        infoGroup.setBackground(Color.WHITE);
        infoGroup.setBorder(BorderFactory.createTitledBorder("申请人信息"));
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(8, 15, 8, 15);
        gbcInfo.anchor = GridBagConstraints.WEST;
        JLabel lblName = new JLabel("姓名：" + user.getUsername());
        lblName.setFont(UIUtil.FONT_BODY);
        gbcInfo.gridx = 0; gbcInfo.gridy = 0;
        infoGroup.add(lblName, gbcInfo);
        JLabel lblPhone = new JLabel("电话：" + (user.getPhone() != null ? user.getPhone() : ""));
        lblPhone.setFont(UIUtil.FONT_BODY);
        gbcInfo.gridx = 1; gbcInfo.gridy = 0;
        infoGroup.add(lblPhone, gbcInfo);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(infoGroup, gbc);

        // 地块信息组
        JPanel landGroup = new JPanel(new GridBagLayout());
        landGroup.setBackground(Color.WHITE);
        landGroup.setBorder(BorderFactory.createTitledBorder("地块信息"));
        gbcInfo.gridx = 0; gbcInfo.gridy = 0;
        JLabel lblArea = new JLabel("面积(㎡)：");
        lblArea.setFont(UIUtil.FONT_BODY);
        landGroup.add(lblArea, gbcInfo);
        tfArea = new JTextField(10);
        tfArea.setFont(UIUtil.FONT_BODY);
        tfArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbcInfo.gridx = 1;
        landGroup.add(tfArea, gbcInfo);

        JLabel lblPurpose = new JLabel("用途：");
        lblPurpose.setFont(UIUtil.FONT_BODY);
        gbcInfo.gridx = 0; gbcInfo.gridy = 1;
        landGroup.add(lblPurpose, gbcInfo);
        tfPurpose = new JTextField(15);
        tfPurpose.setFont(UIUtil.FONT_BODY);
        tfPurpose.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbcInfo.gridx = 1;
        landGroup.add(tfPurpose, gbcInfo);
        gbc.gridy = 1;
        card.add(landGroup, gbc);

        // 附件信息组
        JPanel attachGroup = new JPanel(new GridBagLayout());
        attachGroup.setBackground(Color.WHITE);
        attachGroup.setBorder(BorderFactory.createTitledBorder("附件材料"));
        JLabel lblNote = new JLabel("请在上传附件后，在此处填写附件说明。");
        lblNote.setFont(UIUtil.FONT_SMALL);
        lblNote.setForeground(UIUtil.COLOR_TEXT_HINT);
        attachGroup.add(lblNote);
        gbc.gridy = 2;
        card.add(attachGroup, gbc);

        // 提交按钮
        JButton btnSubmit = new JButton("提交申请");
        btnSubmit.setFont(UIUtil.FONT_BODY);
        btnSubmit.setBackground(UIUtil.COLOR_PRIMARY);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setPreferredSize(new Dimension(160, 42));
        btnSubmit.addActionListener(e -> submit());
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnSubmit, gbc);

        add(card, BorderLayout.CENTER);
        tfArea.setEditable(true);
        tfPurpose.setEditable(true);
        SwingUtilities.invokeLater(() -> tfArea.requestFocusInWindow());
    }

    private void submit() {
        try {
            Application app = new Application();
            app.setUserId(user.getUserId());
            app.setPlotArea(Double.parseDouble(tfArea.getText().trim()));
            app.setPurpose(tfPurpose.getText().trim());
            app.setApplyTime(new Date());
            app.setStatus("待村级审批");
            app.setCurrentApprovalLevel("村级");

            if (appService.submitApplication(app)) {
                // 提交成功后，app对象已包含生成的appId
                UIUtil.showInfo("提交成功！您的申请ID是：" + app.getAppId());
                clearForm();
            } else {
                UIUtil.showError("提交失败！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("面积必须为数字！");
        } catch (Exception e) {
            UIUtil.showError("提交失败：" + e.getMessage());
        }
    }

    private void clearForm() {
        tfArea.setText("");
        tfPurpose.setText("");
        tfArea.requestFocusInWindow();
    }


    public void requestFocusForInput() {
        SwingUtilities.invokeLater(() -> {
            if (tfArea != null) {
                tfArea.requestFocusInWindow();
            }
        });
    }
}