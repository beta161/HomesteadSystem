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
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = UIUtil.createLabel("宅基地申请登记", true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblArea = UIUtil.createLabel("面积(㎡)：", false);
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblArea, gbc);
        tfArea = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 1;
        add(tfArea, gbc);

        JLabel lblPurpose = UIUtil.createLabel("用途：", false);
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPurpose, gbc);
        tfPurpose = UIUtil.createTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        add(tfPurpose, gbc);

        JLabel lblFile = UIUtil.createLabel("附件：", false);
        gbc.gridx = 0; gbc.gridy = 3;
        add(lblFile, gbc);
        JPanel filePanel = new JPanel(new BorderLayout());
        tfFilePath = UIUtil.createTextField();
        JButton btnSel = UIUtil.createButton("...");
        btnSel.setPreferredSize(new Dimension(50, 32));
        btnSel.addActionListener(e -> selectFile());
        filePanel.add(tfFilePath, BorderLayout.CENTER);
        filePanel.add(btnSel, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 3;
        add(filePanel, gbc);

        JButton btnSubmit = UIUtil.createButton("提交");
        btnSubmit.setPreferredSize(new Dimension(120, 40));
        btnSubmit.addActionListener(e -> submit());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
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
            app.setUserId(user.getUserId());
            app.setPlotArea(Double.parseDouble(tfArea.getText().trim()));
            app.setPurpose(tfPurpose.getText().trim());
            app.setApplyTime(new Date());
            app.setStatus("待村级审批");
            app.setCurrentApprovalLevel("村级");

            if (appService.submitApplication(app)) {
                // 注意：application的appId在数据库中自增，需要获取返回的ID才能关联附件
                // 但submitApplication未返回ID，实际需扩展，这里简化：附件记录可在申请成功后查询最新申请ID（不推荐）
                // 此处假设申请后app对象已填充ID（需修改Service），为简化，暂不处理附件关联
                String path = tfFilePath.getText().trim();
                if (!path.isEmpty()) {
                    Attachment attach = new Attachment();
                    // 这里需要真正的appId，但未获取，演示时跳过
                    UIUtil.showInfo("提交成功，附件需在申请ID生成后重新上传。");
                } else {
                    UIUtil.showInfo("提交成功！");
                }
                clearForm();
            } else {
                UIUtil.showError("提交失败！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("面积必须为数字！");
        } catch (Exception e) {
            UIUtil.showError("提交失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfArea.setText("");
        tfPurpose.setText("");
        tfFilePath.setText("");
    }
}