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

        JPanel card = UIUtil.createCardPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // 申请人信息组
        JPanel infoGroup = UIUtil.createTitledPanel("申请人信息");
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(8, 15, 8, 15);
        gbcInfo.anchor = GridBagConstraints.WEST;
        JLabel lblName = UIUtil.createLabel("姓名：" + user.getUsername(), false);
        gbcInfo.gridx = 0; gbcInfo.gridy = 0;
        infoGroup.add(lblName, gbcInfo);
        JLabel lblPhone = UIUtil.createLabel("电话：" + (user.getPhone() != null ? user.getPhone() : ""), false);
        gbcInfo.gridx = 1; gbcInfo.gridy = 0;
        infoGroup.add(lblPhone, gbcInfo);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(infoGroup, gbc);

        // 地块信息组
        JPanel landGroup = UIUtil.createTitledPanel("地块信息");
        gbcInfo.gridx = 0; gbcInfo.gridy = 0;
        JLabel lblArea = UIUtil.createLabel("面积(㎡)：", false);
        landGroup.add(lblArea, gbcInfo);
        tfArea = UIUtil.createTextField();
        gbcInfo.gridx = 1;
        landGroup.add(tfArea, gbcInfo);

        JLabel lblPurpose = UIUtil.createLabel("用途：", false);
        gbcInfo.gridx = 0; gbcInfo.gridy = 1;
        landGroup.add(lblPurpose, gbcInfo);
        tfPurpose = UIUtil.createTextField();
        gbcInfo.gridx = 1;
        landGroup.add(tfPurpose, gbcInfo);
        gbc.gridy = 1;
        card.add(landGroup, gbc);

        // 附件信息组（可扩展）
        JPanel attachGroup = UIUtil.createTitledPanel("附件材料");
        JLabel lblNote = new JLabel("请在上传附件后，在此处填写附件说明。");
        lblNote.setFont(UIUtil.FONT_SMALL);
        lblNote.setForeground(UIUtil.COLOR_TEXT_HINT);
        attachGroup.add(lblNote);
        gbc.gridy = 2;
        card.add(attachGroup, gbc);

        // 提交按钮
        JButton btnSubmit = UIUtil.createButton("提交申请");
        btnSubmit.setPreferredSize(new Dimension(160, 42));
        btnSubmit.addActionListener(e -> submit());
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnSubmit, gbc);

        add(card, BorderLayout.CENTER);
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
                UIUtil.showInfo("提交成功！");
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
    }
}