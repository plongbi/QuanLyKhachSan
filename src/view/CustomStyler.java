package view; // Phải cùng package với MainForm

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Lớp tiện ích để style các components Swing theo màu xanh lá
 */
public class CustomStyler {

    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_HEADER = MainForm.COLOR_HEADER;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_TEXT = MainForm.COLOR_TEXT;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Tạo một JLabel đã được style
     */
    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT);
        return label;
    }

    /**
     * Style một JTextField đã có
     */
    public static JTextField createStyledTextField(JTextField textField) {
        textField.setFont(FONT_FIELD);
        textField.setForeground(COLOR_TEXT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return textField;
    }

    /**
     * Tạo một JButton đã được style
     */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm hiệu ứng khi hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_HEADER); // Đậm hơn khi hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });

        return button;
    }

    /**
     * Style một JTable đã có
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_FIELD);
        table.setForeground(COLOR_TEXT);
        table.setRowHeight(28);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(COLOR_BACKGROUND);
        table.setSelectionForeground(COLOR_PRIMARY);

        // Style Header của Bảng
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(COLOR_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));
    }
}