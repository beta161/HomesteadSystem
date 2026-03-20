package com.homestead.ui;

import com.homestead.entity.Application;
import com.homestead.entity.Attachment;
import com.homestead.entity.User;
import com.homestead.service.ApplicationService;
import com.homestead.service.AttachmentService;
import com.homestead.service.impl.ApplicationServiceImpl;
import com.homestead.service.impl.AttachmentServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;

public class ApplyFrame extends JPanel {
    private User user;
    private JTextField tfAppId;
    private JTextField tfArea;
    private JTextField tfPurpose;
    private JTextField tfFilePath;
    private ApplicationService appService;
    private AttachmentService attachService;

    public ApplyFrame(User user) {
        this.user = user;
        this.appService = new ApplicationServiceImpl();
        this.attachService = new AttachmentServiceImpl();
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(UIUtil.COLOR_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = UIUtil.createLabel("宅基地申请登记", true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
        gbc.gridx = 0; gbc.gridy = 1; add(lblAppId, gbc);
        tfAppId = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 1; add(tfAppId, gbc);

        JLabel lblArea = UIUtil.createLabel("面积(㎡)：", false);
        gbc.gridx = 0; gbc.gridy = 2; add(lblArea, gbc);
        tfArea = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 2; add(tfArea, gbc);

        JLabel lblPurpose = UIUtil.createLabel("用途：", false);
        gbc.gridx = 0; gbc.gridy = 3; add(lblPurpose, gbc);
        tfPurpose = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 3; add(tfPurpose, gbc);

        JLabel lblFile = UIUtil.createLabel("附件：", false);
        gbc.gridx = 0; gbc.gridy = 4; add(lblFile, gbc);

        JPanel filePanel = new JPanel(new BorderLayout());
        tfFilePath = UIUtil.createTextField(); // 直接用，去掉setHint
        JButton btnSel = new JButton("...");
        btnSel.addActionListener(e -> selectFile());
        filePanel.add(tfFilePath, BorderLayout.CENTER);
        filePanel.add(btnSel, BorderLayout.EAST);

        gbc.gridx = 1; gbc.gridy = 4; add(filePanel, gbc);

        JButton btnSubmit = UIUtil.createButton("提交");
        btnSubmit.addActionListener(e -> submit());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(btnSubmit, gbc);
    }

    private void selectFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            tfFilePath.setText(f.getAbsolutePath());
        }
    }

    private void submit() {
        try {
            Application app = new Application();
            app.setAppId(Integer.parseInt(tfAppId.getText()));
            app.setPlotArea(Double.parseDouble(tfArea.getText()));
            app.setPurpose(tfPurpose.getText());
            app.setAppId(user.getUserId());
            app.setApplyTime(new Date());
            app.setStatus("待村级审批");

            if (appService.submitApplication(app)) {
                // 附件上传
                String path = tfFilePath.getText();
                if (!path.isEmpty()) {
                    Attachment attach = new Attachment();
                    attach.setAppId(app.getAppId());
                    attach.setFilePath(path);
                    attach.setFileName(new File(path).getName());
                    attach.setUploadTime(new Date());
                    attachService.uploadAttachment(attach);
                }
                UIUtil.showInfo("提交成功！");
            } else {
                UIUtil.showError("提交失败！");
            }
        } catch (Exception e) {
            UIUtil.showError("数据格式错误！");
            e.printStackTrace();
        }
    }
}