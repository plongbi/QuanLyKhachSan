package view;

import javax.swing.*;
import java.awt.*;

// Import các repository bạn cần
import repository.IKhachHangRepository;
import repository.KhachHangRepository;
import repository.INhanVienRepository;
import repository.NhanVienRepository;
import repository.IPhongRepository;
import repository.PhongRepository;
import repository.IDichVuRepository;
import repository.DichVuRepository;

public class MainForm extends JFrame {

    // Định nghĩa màu sắc
    public static final Color COLOR_PRIMARY = new Color(76, 175, 80); // Xanh lá chính
    public static final Color COLOR_BACKGROUND = new Color(236, 249, 245); // Xanh lá rất nhạt
    public static final Color COLOR_TEXT = new Color(51, 51, 51); // Màu chữ
    public static final Color COLOR_HEADER = new Color(56, 142, 60); // Xanh lá đậm cho header

    // Khai báo các Repository
    private IKhachHangRepository khachHangRepo;
    private INhanVienRepository nhanVienRepo;
    private IPhongRepository phongRepo;
    private IDichVuRepository dichVuRepo;

    public MainForm() {
        // Khởi tạo các repository
        khachHangRepo = new KhachHangRepository();
        nhanVienRepo = new NhanVienRepository();
        phongRepo = new PhongRepository();
        ((PhongRepository) phongRepo).khoiTaoPhongChoMenu();
        dichVuRepo = new DichVuRepository();

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

        // --- Tab Khách Hàng ---
        JPanel khachHangTab = new KhachHangPanel(khachHangRepo);
        tabbedPane.addTab("  Quản lý Khách Hàng  ", khachHangTab);

        // --- Tab Nhân Viên ---
        JPanel nhanVienTab = new NhanVienPanel(nhanVienRepo);
        tabbedPane.addTab("  Quản lý Nhân Viên  ", nhanVienTab);

        // --- Tab Dịch Vụ ---
        JPanel dichVuTab = new DichVuPanel(dichVuRepo);
        tabbedPane.addTab("  Quản lý Dịch Vụ  ", null, dichVuTab, "Danh mục dịch vụ khách sạn");

        // --- Tab Phòng ---
        JPanel phongTab = new PhongPanel(phongRepo, khachHangRepo, dichVuRepo);
        tabbedPane.addTab("  Quản lý Phòng  ", null, phongTab, "Sơ đồ phòng và đặt phòng");

        // --- Tab Thanh Toán  ---
        JPanel thanhToanTab = new ThanhToanPanel(phongRepo, dichVuRepo);
        tabbedPane.addTab("  Thanh Toán   ", null, thanhToanTab, "Lập hóa đơn và thanh toán");

        // --- Tab Doanh Thu  ---
        JPanel thongKeTab = new DoanhThuPanel();
        tabbedPane.addTab("  Doanh Thu  ", null, thongKeTab, "Xem doanh thu theo tuần/năm");
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Hàm này dùng để tạo một panel giữ chỗ cho các tab chưa được phát triển
     */
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
//        SwingUtilities.invokeLater(() -> {
//            new MainForm().setVisible(true);
//        });


        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();

            // Xử lý sự kiện khi nhấn nút Đăng Nhập
            loginView.setLoginHandler((username, password, remember) -> {
                String passStr = new String(password);

                // --- Logic kiểm tra đăng nhập đơn giản (Hardcode demo) ---
                // Bạn có thể thay bằng gọi NhanVienRepository để check DB
                if (username.equals("admin") && passStr.equals("1234")) {
                    // Đăng nhập thành công
                    loginView.dispose(); // Đóng màn hình login
                    new MainForm().setVisible(true); // Mở màn hình chính
                } else {
                    loginView.showError("Sai tên đăng nhập hoặc mật khẩu!");
                }
            });

            loginView.setVisible(true);
        });
    }
}