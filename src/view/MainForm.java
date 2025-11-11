package view; // Đảm bảo file này nằm trong package 'view'

import javax.swing.*;
import java.awt.*;

// Import các repository bạn cần
import repository.IKhachHangRepository;
import repository.KhachHangRepository;
import repository.INhanVienRepository; // <-- Đã import
import repository.NhanVienRepository; // <-- Đã import

public class MainForm extends JFrame {

    // Định nghĩa màu xanh lá cây
    public static final Color COLOR_PRIMARY = new Color(76, 175, 80); // Xanh lá chính
    public static final Color COLOR_BACKGROUND = new Color(236, 249, 245); // Xanh lá rất nhạt
    public static final Color COLOR_TEXT = new Color(51, 51, 51); // Màu chữ
    public static final Color COLOR_HEADER = new Color(56, 142, 60); // Xanh lá đậm cho header

    // Khai báo các Repository
    private IKhachHangRepository khachHangRepo;
    private INhanVienRepository nhanVienRepo; // <-- Đã khai báo

    public MainForm() {
        // Khởi tạo các repository
        khachHangRepo = new KhachHangRepository();
        nhanVienRepo = new NhanVienRepository(); // <-- Đã khởi tạo

        // --- Cài đặt JFrame ---
        setTitle("Hệ thống Quản lý Khách sạn");
        setSize(1200, 800);
        setLocationRelativeTo(null); // Giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Tạo JTabbedPane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(COLOR_HEADER); // Màu nền của các tab
        tabbedPane.setForeground(Color.WHITE); // Màu chữ của các tab

        // --- Tab Khách Hàng (Hoạt động) ---
        JPanel khachHangTab = new KhachHangPanel(khachHangRepo);
        tabbedPane.addTab("  Quản lý Khách Hàng  ", khachHangTab);

        // --- Tab Nhân Viên (Đã thay thế) ---
        JPanel nhanVienTab = new NhanVienPanel(nhanVienRepo); // <-- ĐÃ SỬA
        tabbedPane.addTab("  Quản lý Nhân Viên  ", nhanVienTab);

        // --- Tab Phòng (Tạm thời) ---
        JPanel phongTab = createPlaceholderPanel("Chức năng Quản lý Phòng");
        tabbedPane.addTab("  Quản lý Phòng  ", phongTab);

        // --- Tab Thanh Toán (Tạm thời) ---
        JPanel thanhToanTab = createPlaceholderPanel("Chức năng Quản lý Thanh Toán");
        tabbedPane.addTab("  Thanh toán & Doanh thu  ", thanhToanTab);

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BACKGROUND);
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(COLOR_PRIMARY);
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    public static void main(String[] args) {
        // Chạy giao diện
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}