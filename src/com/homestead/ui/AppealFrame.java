package com.homestead.ui;

import com.homestead.entity.Appeal;
import com.homestead.entity.User;
import com.homestead.service.AppealService;
import com.homestead.service.impl.AppealServiceImpl;
import com.homestead.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AppealFrame extends JPanel {
    private User user;
    private boolean isAdminMode;
    private JTextField tfAppId;
    private JTextArea taReason;
    private JTable table;
    private DefaultTableModel model;
    private AppealService appealService;

    public AppealFrame(User user, boolean isAdminMode) {
        this.user = user;
        this.isAdminMode = isAdminMode;
        this.appealService = new AppealServiceImpl();
        initUI();
        if (isAdminMode) loadPendingAppeals();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.COLOR_WHITE);

        if (isAdminMode) {
            // 管理员模式：显示待处理申诉列表并提供处理按钮
            JLabel lblTitle = UIUtil.createLabel("待处理申诉列表", true);
            lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            add(lblTitle, BorderLayout.NORTH);

            String[] columns = {"申诉ID", "申请ID", "申诉理由", "申诉时间", "操作"};
            model = new DefaultTableModel(columns, 0);
            table = UIUtil.createTable();
            table.setModel(model);
            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnRefresh = UIUtil.createButton("刷新");
            btnRefresh.addActionListener(e -> loadPendingAppeals());
            JButton btnHandle = UIUtil.createButton("处理申诉");
            btnHandle.addActionListener(e -> handleAppeal());
            btnPanel.add(btnRefresh);
            btnPanel.add(btnHandle);
            add(btnPanel, BorderLayout.SOUTH);
        } else {
            // 普通用户模式：提交申诉表单
            JPanel formPanel = UIUtil.createTitledPanel("提交申诉", new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 15, 8, 15);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblAppId = UIUtil.createLabel("申请ID：", false);
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(lblAppId, gbc);
            tfAppId = UIUtil.createTextField();
            gbc.gridx = 1; gbc.gridy = 0;
            formPanel.add(tfAppId, gbc);

            JLabel lblReason = UIUtil.createLabel("申诉理由：", false);
            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(lblReason, gbc);
            taReason = new JTextArea(4, 20);
            taReason.setFont(UIUtil.FONT_NORMAL);
            taReason.setLineWrap(true);
            taReason.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIUtil.COLOR_BORDER),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
            ));
            JScrollPane scrollPane = new JScrollPane(taReason);
            scrollPane.setPreferredSize(new Dimension(300, 80));
            gbc.gridx = 1; gbc.gridy = 1;
            formPanel.add(scrollPane, gbc);

            JButton btnSubmit = UIUtil.createButton("提交申诉");
            btnSubmit.addActionListener(e -> submitAppeal());
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(btnSubmit, gbc);

            add(formPanel, BorderLayout.CENTER);
        }
    }

    private void submitAppeal() {
        String appIdStr = tfAppId.getText().trim();
        String reason = taReason.getText().trim();

        if (appIdStr.isEmpty() || reason.isEmpty()) {
            UIUtil.showError("申请ID和申诉理由不能为空！");
            return;
        }

        try {
            Appeal appeal = new Appeal();
            appeal.setAppId(Integer.parseInt(appIdStr));
            appeal.setAppealReason(reason);
            appeal.setAppealTime(new Date());

            if (appealService.submitAppeal(appeal)) {
                UIUtil.showInfo("申诉提交成功！");
                tfAppId.setText("");
                taReason.setText("");
            } else {
                UIUtil.showError("提交失败！");
            }
        } catch (NumberFormatException e) {
            UIUtil.showError("申请ID必须为整数！");
        }
    }

    private void loadPendingAppeals() {
        model.setRowCount(0);
        List<Appeal> list = appealService.getPendingAppeals();
        if (list != null && !list.isEmpty()) {
            for (Appeal a : list) {
                String timeStr = a.getAppealTime() != null ? a.getAppealTime().toLocaleString() : "";
                model.addRow(new Object[]{
                        a.getAppealId(),
                        a.getAppId(),
                        a.getAppealReason(),
                        timeStr,
                        "待处理"
                });
            }
        }
    }

    private void handleAppeal() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIUtil.showError("请先选择一条申诉！");
            return;
        }
        Integer appealId = (Integer) model.getValueAt(row, 0);
        String[] options = {"通过", "驳回"};
        int choice = JOptionPane.showOptionDialog(this, "请选择处理结果：", "处理申诉",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == JOptionPane.CLOSED_OPTION) return;

        String result = (choice == 0) ? "通过" : "驳回";
        String opinion = JOptionPane.showInputDialog(this, "请输入处理意见：", "处理申诉", JOptionPane.PLAIN_MESSAGE);
        if (opinion == null) return;

        boolean success = appealService.handleAppeal(appealId, result, opinion);
        if (success) {
            UIUtil.showInfo("处理成功！");
            loadPendingAppeals();
        } else {
            UIUtil.showError("处理失败！");
        }
    }
}