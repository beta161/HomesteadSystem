package com.homestead.util;

import javax.swing.*;
import java.awt.*;

public class UIUtil {
    // 颜色
    public static final Color COLOR_MAIN = new Color(51, 102, 204);
    public static final Color COLOR_LIGHT = new Color(245, 245, 245);
    public static final Color COLOR_WHITE = Color.WHITE;

    // 字体（统一用微软雅黑）
    public static final Font FONT_TITLE = new Font("微软雅黑", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("微软雅黑", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("微软雅黑", Font.PLAIN, 12);

    /**
     * 创建按钮（去掉 FONT_BUTTON）
     */
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NORMAL); // 直接用 FONT_NORMAL
        btn.setBackground(COLOR_MAIN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    /**
     * 创建文本框（支持占位提示文字 - 修复版）
     */
    public static JTextField createTextField(String hint) {
        JTextField tf = new JTextField(hint);
        tf.setFont(FONT_NORMAL);
        tf.setPreferredSize(new Dimension(200, 30));
        tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return tf;
    }

    // 兼容旧代码
    public static JTextField createTextField() {
        return createTextField("");
    }

    /**
     * 创建标签
     */
    public static JLabel createLabel(String text, boolean isTitle) {
        JLabel lbl = new JLabel(text);
        if (isTitle) lbl.setFont(FONT_TITLE);
        else lbl.setFont(FONT_NORMAL);
        return lbl;
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