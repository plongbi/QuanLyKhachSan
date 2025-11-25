package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.DichVu;
import model.KhachHang;
import model.Phong;
import repository.IDichVuRepository;
import repository.IKhachHangRepository;
import repository.IPhongRepository;

public class PhongPanel extends JPanel {

    // Các Repository để thao tác dữ liệu
    private IPhongRepository phongRepo;
    private IKhachHangRepository khachHangRepo;
    private IDichVuRepository dichVuRepo;

    // Các thành phần giao diện chính
    private JPanel roomCardsPanel; // Panel chứa các thẻ phòng
    private DecimalFormat vndFormat; // Định dạng tiền tệ (ví dụ: 100.000 VND)

    // --- ĐỊNH NGHĨA MÀU SẮC GIAO DIỆN ---
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_AVAILABLE_BG = new Color(240, 255, 240); // Xanh nhạt (Phòng trống)
    private static final Color COLOR_RENTED_BG = new Color(255, 240, 240);    // Đỏ nhạt (Đã thuê)
    private static final Color COLOR_BORDER = new Color(200, 200, 200);
    private static final Color COLOR_BUTTON_AVAILABLE = new Color(46, 204, 113); // Nút Đặt phòng (Xanh lá)
    private static final Color COLOR_BUTTON_RENTED = new Color(52, 152, 219);    // Nút Chi tiết (Xanh dương)

    // === CONSTRUCTOR ===
    public PhongPanel(IPhongRepository phongRepo, IKhachHangRepository khachHangRepo, IDichVuRepository dichVuRepo) {
        this.phongRepo = phongRepo;
        this.khachHangRepo = khachHangRepo;
        this.dichVuRepo = dichVuRepo;

        // Cài đặt định dạng tiền tệ Việt Nam
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        // Cấu hình layout chính
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();   // Vẽ giao diện khung
        loadPhongCards();   // Tải dữ liệu phòng từ DB lên giao diện
    }

    // === KHỞI TẠO CÁC THÀNH PHẦN GIAO DIỆN ===
    private void initComponents() {
        // 1. Header (Tiêu đề và Nút làm mới)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Sơ Đồ Phòng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(MainForm.COLOR_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton btnRefresh = CustomStyler.createStyledButton("Làm Mới");
        btnRefresh.setPreferredSize(new Dimension(120, 40));
        btnRefresh.addActionListener(e -> loadPhongCards()); // Sự kiện click nút Làm mới

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnRefresh);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Body (Khu vực chứa danh sách các thẻ phòng)
        roomCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        roomCardsPanel.setBackground(COLOR_BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(roomCardsPanel);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn chuột

        add(scrollPane, BorderLayout.CENTER);
    }

    // === TẢI DANH SÁCH PHÒNG TỪ DATABASE ===
    private void loadPhongCards() {
        roomCardsPanel.removeAll(); // Xóa các thẻ cũ trước khi vẽ lại
        List<Phong> danhSachPhong = phongRepo.getAll();

        if (danhSachPhong.isEmpty()) {
            JLabel lblTrong = new JLabel("Chưa có phòng nào trong hệ thống.", SwingConstants.CENTER);
            lblTrong.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            roomCardsPanel.setLayout(new BorderLayout());
            roomCardsPanel.add(lblTrong, BorderLayout.CENTER);
        } else {
            roomCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
            for (Phong phong : danhSachPhong) {
                // Tạo thẻ giao diện cho từng phòng
                JPanel card = createRoomCard(phong);
                roomCardsPanel.add(card);
            }
        }
        // Cập nhật lại giao diện sau khi thêm component
        roomCardsPanel.revalidate();
        roomCardsPanel.repaint();
    }

    // === TẠO GIAO DIỆN CHO 1 THẺ PHÒNG (ROOM CARD) ===
    private JPanel createRoomCard(Phong phong) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(220, 200));

        // -- Phần trên: Mã phòng và Trạng thái --
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblMaPhong = new JLabel(phong.getMaPhong());
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblMaPhong.setForeground(new Color(50, 50, 50));

        JLabel lblStatus = new JLabel("", SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        header.add(lblMaPhong, BorderLayout.WEST);
        header.add(lblStatus, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // -- Phần giữa: Thông tin chi tiết --
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblLoai = new JLabel(phong.getLoaiPhong());
        lblLoai.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblGia = new JLabel(vndFormat.format(phong.getGiaPhong()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGia.setForeground(MainForm.COLOR_PRIMARY);

        JLabel lblKhach = new JLabel(" ");
        lblKhach.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        body.add(lblLoai);
        body.add(Box.createVerticalStrut(5));
        body.add(lblGia);
        body.add(Box.createVerticalStrut(5));
        body.add(lblKhach);

        card.add(body, BorderLayout.CENTER);

        // -- Phần dưới: Nút hành động --
        JButton btnAction = new JButton();
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(btnAction);
        card.add(footer, BorderLayout.SOUTH);

        // -- Xử lý logic hiển thị theo trạng thái phòng --
        if (phong.isTrangThai()) {
            // Phòng ĐÃ THUÊ
            card.setBackground(COLOR_RENTED_BG);
            lblStatus.setText("Đã Thuê");
            lblStatus.setForeground(Color.RED);

            btnAction.setText("Chi Tiết");
            btnAction.setBackground(COLOR_BUTTON_RENTED);

            if (phong.getKhachThue() != null) {
                lblKhach.setText("Khách: " + phong.getKhachThue().getTen());
            } else {
                lblKhach.setText("Khách: [Không rõ]");
            }

            // Sự kiện: Xem chi tiết
            btnAction.addActionListener(e -> hienThiChiTietPhong(phong));

        } else {
            // Phòng TRỐNG
            card.setBackground(COLOR_AVAILABLE_BG);
            lblStatus.setText("Trống");
            lblStatus.setForeground(COLOR_BUTTON_AVAILABLE);

            btnAction.setText("Đặt Phòng");
            btnAction.setBackground(COLOR_BUTTON_AVAILABLE);
            lblKhach.setText(" "); // Giữ chỗ layout

            // Sự kiện: Đặt phòng
            btnAction.addActionListener(e -> datPhong(phong));
        }

        return card;
    }

    // === CHỨC NĂNG 1: HIỂN THỊ CHI TIẾT & QUẢN LÝ DỊCH VỤ ===
    private void hienThiChiTietPhong(Phong phong) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(600, 400));

        // 1. Thông tin khách hàng
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Phòng & Khách Hàng"));

        infoPanel.add(new JLabel("Phòng: " + phong.getMaPhong() + " - Loại: " + phong.getLoaiPhong()));
        infoPanel.add(new JLabel("Giá phòng: " + vndFormat.format(phong.getGiaPhong()) + "/ngày"));

        String tenKhach = (phong.getKhachThue() != null) ? phong.getKhachThue().getTen() : "---";
        String cmnd = (phong.getKhachThue() != null) ? phong.getKhachThue().getSoCMND() : "---";
        infoPanel.add(new JLabel("Khách đang thuê: " + tenKhach));
        infoPanel.add(new JLabel("Số CMND: " + cmnd));

        panel.add(infoPanel, BorderLayout.NORTH);

        // 2. Bảng dịch vụ phòng này đang dùng
        String[] cols = {"Tên Dịch Vụ", "Đơn Giá"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        CustomStyler.styleTable(table);

        // Lọc dịch vụ từ DB: Chỉ lấy các dòng có MaPhong trùng với phòng này
        List<DichVu> allServices = dichVuRepo.getAll();
        double tongTienDichVu = 0;

        for (DichVu dv : allServices) {
            if (dv.getMaPhong() != null && dv.getMaPhong().equals(phong.getMaPhong())) {
                tableModel.addRow(new Object[]{dv.getTenDV(), vndFormat.format(dv.getGiaDV())});
                tongTienDichVu += dv.getGiaDV();
            }
        }

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Dịch Vụ Đang Sử Dụng (Tổng: " + vndFormat.format(tongTienDichVu) + ")"));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // 3. Nút thêm dịch vụ
        JButton btnAddService = CustomStyler.createStyledButton("Thêm Dịch Vụ Mới");
        btnAddService.addActionListener(e -> themDichVuVaoPhong(phong, tableModel));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnAddService);
        panel.add(btnPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Chi Tiết Phòng " + phong.getMaPhong(), JOptionPane.PLAIN_MESSAGE);
    }

    // === CHỨC NĂNG 2: THÊM DỊCH VỤ (Sao chép từ Menu sang Phòng) ===
    private void themDichVuVaoPhong(Phong phong, DefaultTableModel tableModel) {
        // B1: Lấy danh sách Menu (những món có MaPhong = "MENU")
        List<DichVu> allServices = dichVuRepo.getAll();
        List<DichVu> menuList = new ArrayList<>();

        for (DichVu dv : allServices) {
            if (dv.getMaPhong() != null && dv.getMaPhong().equalsIgnoreCase("MENU")) {
                menuList.add(dv);
            }
        }

        if (menuList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có món nào trong Menu (Tab Dịch Vụ)!");
            return;
        }

        // B2: Hiển thị ComboBox cho người dùng chọn
        JComboBox<DichVu> combo = new JComboBox<>(menuList.toArray(new DichVu[0]));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof DichVu) {
                    DichVu d = (DichVu)value;
                    setText(d.getTenDV() + " - " + vndFormat.format(d.getGiaDV()));
                }
                return this;
            }
        });

        int result = JOptionPane.showConfirmDialog(this, combo, "Chọn món thêm vào phòng:", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            DichVu selectedMenu = (DichVu) combo.getSelectedItem();
            if (selectedMenu != null) {
                // B3: TẠO BẢN SAO (QUAN TRỌNG)
                // Tạo một đối tượng Dịch Vụ MỚI, chép tên và giá từ Menu, nhưng gán vào Phòng hiện tại
                DichVu dichVuMoi = new DichVu(
                        selectedMenu.getTenDV(),
                        selectedMenu.getGiaDV(),
                        phong.getMaPhong()
                );

                // Lưu bản sao này vào DB
                if (dichVuRepo.add(dichVuMoi)) {
                    JOptionPane.showMessageDialog(this, "Đã thêm: " + dichVuMoi.getTenDV());
                    // Cập nhật bảng hiển thị ngay lập tức
                    tableModel.addRow(new Object[]{dichVuMoi.getTenDV(), vndFormat.format(dichVuMoi.getGiaDV())});
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi thêm dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // === CHỨC NĂNG 3: ĐẶT PHÒNG ===
    private void datPhong(Phong phong) {
        List<KhachHang> dsKhach = khachHangRepo.getAll();
        if (dsKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có khách hàng nào! Hãy thêm khách trước.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo ComboBox chọn khách
        JComboBox<KhachHang> cbKhach = new JComboBox<>(dsKhach.toArray(new KhachHang[0]));
        cbKhach.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KhachHang) {
                    KhachHang k = (KhachHang) value;
                    setText(k.getTen() + " (CMND: " + k.getSoCMND() + ")");
                }
                return this;
            }
        });

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Chọn khách hàng cho phòng " + phong.getMaPhong() + ":"), BorderLayout.NORTH);
        panel.add(cbKhach, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Đặt Phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            KhachHang khach = (KhachHang) cbKhach.getSelectedItem();
            if (khach != null) {
                // Cập nhật trạng thái phòng
                phong.setTrangThai(true);
                phong.setKhachThue(khach);

                if (phongRepo.update(phong)) {
                    JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
                    loadPhongCards(); // Tải lại giao diện
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}