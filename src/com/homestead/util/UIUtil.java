package com.homestead.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class UIUtil {
    // 颜色体系
    public static final Color COLOR_MAIN = new Color(84, 129, 217);      // 主色调
    public static final Color COLOR_LIGHT = new Color(217, 229, 139);    // 浅灰背景
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(214, 215, 171);   // 边框色
    public static final Color COLOR_ACCENT = new Color(255, 140, 0);     // 强调色（可选）

    // 字体
    public static final Font FONT_TITLE = new Font("微软雅黑", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("微软雅黑", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("微软雅黑", Font.PLAIN, 12);

    /**
     * 创建统一按钮（带悬停效果）
     */
    public static JButton createButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    setBackground(COLOR_MAIN.darker());
                } else {
                    setBackground(COLOR_MAIN);
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_NORMAL);
        btn.setBackground(COLOR_MAIN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    /**
     * 创建文本框（支持占位提示，简化版）
     */
    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_NORMAL);
        tf.setPreferredSize(new Dimension(200, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return tf;
    }

    /**
     * 创建带占位符的文本框（可选）
     */
    public static JTextField createTextField(String hint) {
        JTextField tf = createTextField();
        tf.setText(hint);
        return tf;
    }

    /**
     * 创建标签
     */
    public static JLabel createLabel(String text, boolean isTitle) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(isTitle ? FONT_TITLE : FONT_NORMAL);
        if (!isTitle) {
            lbl.setForeground(Color.DARK_GRAY);
        }
        return lbl;
    }

    /**
     * 创建带标题边框的面板
     */
    public static JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(COLOR_WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_NORMAL
        );
        border.setTitleColor(COLOR_MAIN);
        panel.setBorder(border);
        return panel;
    }

    /**
     * 创建标准表格（统一样式）
     */
    public static JTable createTable() {
        JTable table = new JTable();
        table.setFont(FONT_SMALL);
        table.setRowHeight(30);
        table.setGridColor(COLOR_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(COLOR_LIGHT);
        table.setSelectionForeground(Color.BLACK);
        return table;
    }

    // 弹窗
    public static void showInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(null, msg, "确认", JOptionPane.YES_NO_OPTION);
    }
}