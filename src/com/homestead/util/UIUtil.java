package com.homestead.util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class UIUtil {
    // 主色调（低饱和度政务蓝）
    public static final Color COLOR_PRIMARY = new Color(44, 110, 183);
    public static final Color COLOR_PRIMARY_LIGHT = new Color(70, 130, 200);
    public static final Color COLOR_PRIMARY_DARK = new Color(30, 80, 140);

    // 辅助色
    public static final Color COLOR_BG = new Color(245, 248, 250);
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(220, 225, 230);

    // 功能色
    public static final Color COLOR_SUCCESS = new Color(82, 196, 26);
    public static final Color COLOR_WARNING = new Color(250, 173, 20);
    public static final Color COLOR_DANGER = new Color(245, 108, 108);

    // 文字色
    public static final Color COLOR_TEXT_TITLE = new Color(30, 30, 30);
    public static final Color COLOR_TEXT_BODY = new Color(60, 60, 60);
    public static final Color COLOR_TEXT_HINT = new Color(150, 150, 150);

    // 字体
    public static final Font FONT_TITLE = new Font("微软雅黑", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("微软雅黑", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("微软雅黑", Font.PLAIN, 15);
    public static final Font FONT_SMALL = new Font("微软雅黑", Font.PLAIN, 13);

    public static final int CORNER_RADIUS = 8;

    /**
     * 开启Swing全局字体抗锯齿（应在程序入口调用）
     */
    public static void enableAntiAliasing() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    /**
     * 创建圆角卡片面板
     */
    public static JPanel createCardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 1. 绘制白色圆角背景
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                // 2. 先调用父类绘制子组件
                super.paintComponent(g2);
                // 3. 最后绘制半透明阴影（放在最上层，但透明度低，不影响交互）
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }

    /**
     * 圆角输入框
     */
    public static JTextField createTextField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        tf.setFont(FONT_BODY);
        tf.setBackground(Color.WHITE);
        tf.setForeground(COLOR_TEXT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return tf;
    }

    /**
     * 圆角按钮
     */
    public static JButton createButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(COLOR_PRIMARY_LIGHT);
                } else {
                    g2.setColor(COLOR_PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(100, 38));
        return btn;
    }

    /**
     * 标签
     */
    public static JLabel createLabel(String text, boolean isTitle) {
        JLabel lbl = new JLabel(text);
        if (isTitle) {
            lbl.setFont(FONT_TITLE);
            lbl.setForeground(COLOR_TEXT_TITLE);
        } else {
            lbl.setFont(FONT_BODY);
            lbl.setForeground(COLOR_TEXT_BODY);
        }
        return lbl;
    }

    /**
     * 表格（隔行变色）
     */
    public static JTable createTable() {
        JTable table = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 252));
                }
                return c;
            }
        };
        table.setFont(FONT_SMALL);
        table.setRowHeight(36);
        table.setGridColor(COLOR_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(COLOR_TEXT_BODY);
        table.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        return table;
    }

    /**
     * 带标题边框的分组面板
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_SUBTITLE,
                COLOR_PRIMARY
        );
        border.setTitlePosition(TitledBorder.TOP);
        panel.setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return panel;
    }

    /**
     * 渐变顶部栏
     */
    public static JPanel createGradientHeader() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY_DARK, getWidth(), 0, COLOR_PRIMARY_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 60));
        return panel;
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