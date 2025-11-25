//package view;
//
//import java.awt.*;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.util.List;
//import java.util.Locale;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//
//import model.DichVu; // Import Model Dịch vụ
//import model.Phong;
//import model.ThanhToan;
//import repository.IDichVuRepository; // Import Repository Dịch vụ
//import repository.IPhongRepository;
//
//public class ThanhToanPanel extends JPanel {
//
//    private IPhongRepository phongRepo;
//    private IDichVuRepository dichVuRepo; // Khai báo thêm Repo Dịch vụ
//
//    private JComboBox<Phong> cboPhong;
//    private JTable invoiceTable;
//    private DefaultTableModel invoiceModel;
//    private JLabel lblTongTien, lblKhachHang, lblCheckInTime;
//    private JButton btnThanhToan, btnThemDichVu;
//
//    // Formatter tiền tệ
//    private DecimalFormat vndFormat;
//
//    // Màu sắc
//    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
//    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
//
//    // --- SỬA CONSTRUCTOR: Nhận thêm IDichVuRepository ---
//    public ThanhToanPanel(IPhongRepository phongRepo, IDichVuRepository dichVuRepo) {
//        this.phongRepo = phongRepo;
//        this.dichVuRepo = dichVuRepo; // Gán biến
//
//        // Setup format tiền tệ (dùng dấu . ngăn cách hàng nghìn)
//        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
//        symbols.setGroupingSeparator('.');
//        vndFormat = new DecimalFormat("###,### VND", symbols);
//
//        setLayout(new BorderLayout(15, 15));
//        setBackground(COLOR_BACKGROUND);
//        setBorder(new EmptyBorder(15, 15, 15, 15));
//
//        initComponents();
//        loadOccupiedRooms();
//    }
//
//    private void initComponents() {
//        // === 1. PANEL TRÁI ===
//        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
//        leftPanel.setOpaque(false);
//        leftPanel.setPreferredSize(new Dimension(400, 0));
//
//        // a. Chọn Phòng
//        JPanel selectionPanel = new JPanel(new GridLayout(4, 1, 5, 5));
//        selectionPanel.setOpaque(false);
//        selectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Thông Tin Thanh Toán", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));
//
//        cboPhong = new JComboBox<>();
//        cboPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        cboPhong.addActionListener(e -> updateRoomInfo());
//
//        lblKhachHang = CustomStyler.createStyledLabel("Khách hàng: ---");
//        lblCheckInTime = CustomStyler.createStyledLabel("Trạng thái: Đang thuê");
//
//        selectionPanel.add(CustomStyler.createStyledLabel("Chọn Phòng:"));
//        selectionPanel.add(cboPhong);
//        selectionPanel.add(lblKhachHang);
//        selectionPanel.add(lblCheckInTime);
//
//        // b. Thêm Dịch Vụ
//        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        servicePanel.setOpaque(false);
//        servicePanel.setBorder(BorderFactory.createTitledBorder(null, "Dịch Vụ Thêm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));
//
//        btnThemDichVu = CustomStyler.createStyledButton("Thêm Dịch Vụ");
//        btnThemDichVu.setBackground(new Color(255, 152, 0));
//        btnThemDichVu.addActionListener(e -> actionAddService()); // Gọi hàm xử lý mới
//        servicePanel.add(btnThemDichVu);
//
//        leftPanel.add(selectionPanel, BorderLayout.NORTH);
//        leftPanel.add(servicePanel, BorderLayout.CENTER);
//
//        // === 2. PANEL PHẢI: HÓA ĐƠN ===
//        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
//        rightPanel.setBackground(Color.WHITE);
//        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//
//        JLabel lblInvoiceHeader = new JLabel("CHI TIẾT HÓA ĐƠN", SwingConstants.CENTER);
//        lblInvoiceHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        lblInvoiceHeader.setForeground(COLOR_PRIMARY);
//        lblInvoiceHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
//        rightPanel.add(lblInvoiceHeader, BorderLayout.NORTH);
//
//        // Bảng Hóa Đơn
//        String[] cols = {"Hạng Mục", "Đơn Giá", "Thành Tiền"};
//        invoiceModel = new DefaultTableModel(cols, 0);
//        invoiceTable = new JTable(invoiceModel);
//        CustomStyler.styleTable(invoiceTable);
//        rightPanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);
//
//        // Footer Tổng Tiền
//        JPanel footerPanel = new JPanel(new BorderLayout());
//        footerPanel.setBackground(Color.WHITE);
//        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        lblTongTien = new JLabel("Tổng cộng: 0 VND", SwingConstants.RIGHT);
//        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        lblTongTien.setForeground(new Color(231, 76, 60));
//
//        btnThanhToan = CustomStyler.createStyledButton("THANH TOÁN & TRẢ PHÒNG");
//        btnThanhToan.setPreferredSize(new Dimension(200, 50));
//        btnThanhToan.addActionListener(e -> actionThanhToan());
//
//        footerPanel.add(lblTongTien, BorderLayout.NORTH);
//        footerPanel.add(btnThanhToan, BorderLayout.SOUTH);
//
//        rightPanel.add(footerPanel, BorderLayout.SOUTH);
//
//        add(leftPanel, BorderLayout.WEST);
//        add(rightPanel, BorderLayout.CENTER);
//    }
//
//    // === LOGIC ===
//
//    private void loadOccupiedRooms() {
//        cboPhong.removeAllItems();
//        List<Phong> list = phongRepo.getAll();
//        for (Phong p : list) {
//            if (p.isTrangThai()) {
//                cboPhong.addItem(p);
//            }
//        }
//        // Renderer hiển thị tên phòng đẹp
//        cboPhong.setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                if (value instanceof Phong) {
//                    Phong p = (Phong) value;
//                    setText("Phòng " + p.getMaPhong() + " (" + p.getLoaiPhong() + ")");
//                }
//                return this;
//            }
//        });
//    }
//
//    private void updateRoomInfo() {
//        Phong p = (Phong) cboPhong.getSelectedItem();
//        if (p != null) {
//            String tenKhach = (p.getKhachThue() != null) ? p.getKhachThue().getTen() : "Không rõ";
//            lblKhachHang.setText("Khách hàng: " + tenKhach);
//
//            // Reset bảng và thêm tiền phòng
//            invoiceModel.setRowCount(0);
//            double giaPhong = p.getGiaPhong();
//            invoiceModel.addRow(new Object[]{
//                    "Tiền thuê phòng (" + p.getLoaiPhong() + ")",
//                    vndFormat.format(giaPhong),
//                    vndFormat.format(giaPhong)
//            });
//
//            recalculateTotal();
//        }
//    }
//
//    // --- SỬA ĐỔI: Lấy dịch vụ từ DB ---
//    private void actionAddService() {
//        // 1. Kiểm tra xem đã chọn phòng chưa
//        if (cboPhong.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng trước khi thêm dịch vụ!");
//            return;
//        }
//
//        // 2. Lấy danh sách dịch vụ từ Repository
//        List<DichVu> listDichVu = dichVuRepo.getAll();
//
//        if (listDichVu.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Chưa có dịch vụ nào trong hệ thống!");
//            return;
//        }
//
//        // 3. Tạo JComboBox để hiển thị trong Dialog
//        JComboBox<DichVu> comboDichVu = new JComboBox<>(listDichVu.toArray(new DichVu[0]));
//
//        // Custom hiển thị cho combobox dịch vụ (Tên - Giá)
//        comboDichVu.setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                if (value instanceof DichVu) {
//                    DichVu dv = (DichVu) value;
//                    setText(dv.getTenDV() + " - " + vndFormat.format(dv.getGiaDV()));
//                }
//                return this;
//            }
//        });
//
//        // 4. Hiển thị Dialog chọn
//        Object[] message = {
//                "Chọn dịch vụ muốn thêm:", comboDichVu
//        };
//
//        int option = JOptionPane.showConfirmDialog(this, message, "Thêm Dịch Vụ", JOptionPane.OK_CANCEL_OPTION);
//
//        if (option == JOptionPane.OK_OPTION) {
//            DichVu selectedDV = (DichVu) comboDichVu.getSelectedItem();
//            if (selectedDV != null) {
//                // Thêm vào bảng hóa đơn
//                invoiceModel.addRow(new Object[]{
//                        selectedDV.getTenDV(),
//                        vndFormat.format(selectedDV.getGiaDV()),
//                        vndFormat.format(selectedDV.getGiaDV()) // Tạm thời số lượng là 1
//                });
//                // Tính lại tổng tiền
//                recalculateTotal();
//            }
//        }
//    }
//
//    // --- SỬA ĐỔI: Hàm tính tổng tiền hoàn chỉnh ---
//    private void recalculateTotal() {
//        double total = 0;
//        for (int i = 0; i < invoiceModel.getRowCount(); i++) {
//            // Lấy chuỗi tiền từ cột thứ 3 (index 2)
//            String moneyString = invoiceModel.getValueAt(i, 2).toString();
//            try {
//                // Parse chuỗi "100.000 VND" thành số double
//                Number number = vndFormat.parse(moneyString);
//                total += number.doubleValue();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        lblTongTien.setText("Tổng cộng: " + vndFormat.format(total));
//    }
//
//    private void actionThanhToan() {
//        Phong p = (Phong) cboPhong.getSelectedItem();
//        if (p == null) return;
//
//        // Lấy tổng tiền hiện tại từ nhãn (đã tính cả dịch vụ)
//        String tongTienStr = lblTongTien.getText().replace("Tổng cộng: ", "");
//
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Xác nhận thanh toán cho phòng " + p.getMaPhong() + "?\nTổng tiền: " + tongTienStr,
//                "Xác nhận", JOptionPane.YES_NO_OPTION);
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            // Gọi hàm ghi nhận thanh toán (model)
//            String msg = ThanhToan.ghiNhanThanhToan(p);
//
//            // Cập nhật DB
//            p.setTrangThai(false);
//            p.setKhachThue(null);
//            phongRepo.update(p);
//
//            JOptionPane.showMessageDialog(this, msg + "\nTổng thực thu: " + tongTienStr);
//
//            // Reset giao diện
//            loadOccupiedRooms();
//            invoiceModel.setRowCount(0);
//            lblTongTien.setText("Tổng cộng: 0 VND");
//        }
//    }
//}

package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList; // Import thêm
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import model.DichVu;
import model.Phong;
import model.ThanhToan;
import repository.IDichVuRepository;
import repository.IPhongRepository;

public class ThanhToanPanel extends JPanel {

    private IPhongRepository phongRepo;
    private IDichVuRepository dichVuRepo;

    private JComboBox<Phong> cboPhong;
    private JTable invoiceTable;
    private DefaultTableModel invoiceModel;
    private JLabel lblTongTien, lblKhachHang;
    private JButton btnThanhToan;

    // Danh sách lưu tạm các dịch vụ của phòng đang chọn để tiện xóa sau khi thanh toán
    private List<DichVu> dichVuCuaPhong = new ArrayList<>();

    private DecimalFormat vndFormat;
    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;

    public ThanhToanPanel(IPhongRepository phongRepo, IDichVuRepository dichVuRepo) {
        this.phongRepo = phongRepo;
        this.dichVuRepo = dichVuRepo;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        initComponents();
        loadOccupiedRooms();
    }

    private void initComponents() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));

        JPanel selectionPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Thông Tin Phòng", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        cboPhong = new JComboBox<>();
        cboPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboPhong.addActionListener(e -> updateInvoiceInfo());

        lblKhachHang = CustomStyler.createStyledLabel("Khách hàng: ---");

        JButton btnLamMoi = CustomStyler.createStyledButton("Làm Mới Danh Sách");
        btnLamMoi.setBackground(new Color(255, 152, 0));
        btnLamMoi.addActionListener(e -> loadOccupiedRooms());

        selectionPanel.add(CustomStyler.createStyledLabel("Chọn Phòng Cần Thanh Toán:"));
        selectionPanel.add(cboPhong);
        selectionPanel.add(lblKhachHang);
        selectionPanel.add(btnLamMoi);

        leftPanel.add(selectionPanel, BorderLayout.NORTH);

        // Không cần nút thêm dịch vụ ở đây nữa vì đã thêm bên Chi Tiết Phòng

        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblInvoiceHeader = new JLabel("CHI TIẾT HÓA ĐƠN", SwingConstants.CENTER);
        lblInvoiceHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblInvoiceHeader.setForeground(COLOR_PRIMARY);
        lblInvoiceHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        rightPanel.add(lblInvoiceHeader, BorderLayout.NORTH);

        String[] cols = {"Nội Dung", "Đơn Giá", "Thành Tiền"};
        invoiceModel = new DefaultTableModel(cols, 0);
        invoiceTable = new JTable(invoiceModel);
        CustomStyler.styleTable(invoiceTable);

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblTongTien = new JLabel("Tổng cộng: 0 VND", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTongTien.setForeground(new Color(231, 76, 60));

        btnThanhToan = CustomStyler.createStyledButton("THANH TOÁN & TRẢ PHÒNG");
        btnThanhToan.setPreferredSize(new Dimension(250, 50));
        btnThanhToan.addActionListener(e -> xuLyThanhToan());

        footerPanel.add(lblTongTien, BorderLayout.NORTH);
        footerPanel.add(btnThanhToan, BorderLayout.SOUTH);

        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadOccupiedRooms() {
        cboPhong.removeAllItems();
        List<Phong> list = phongRepo.getAll();
        boolean hasRoom = false;
        for (Phong p : list) {
            if (p.isTrangThai()) {
                cboPhong.addItem(p);
                hasRoom = true;
            }
        }

        cboPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Phong) {
                    Phong p = (Phong) value;
                    setText("Phòng " + p.getMaPhong() + " - " + p.getLoaiPhong());
                }
                return this;
            }
        });

        if (!hasRoom) {
            lblKhachHang.setText("Không có phòng nào đang thuê.");
            invoiceModel.setRowCount(0);
            lblTongTien.setText("Tổng cộng: 0 VND");
        }
    }

    // LOGIC MỚI: Tự động load dịch vụ từ DB
    private void updateInvoiceInfo() {
        Phong p = (Phong) cboPhong.getSelectedItem();
        if (p != null) {
            String tenKhach = (p.getKhachThue() != null) ? p.getKhachThue().getTen() : "Không rõ";
            lblKhachHang.setText("Khách hàng: " + tenKhach);

            invoiceModel.setRowCount(0);
            dichVuCuaPhong.clear(); // Xóa list tạm cũ

            double tongTien = 0;

            // 1. Tiền phòng
            double giaPhong = p.getGiaPhong();
            invoiceModel.addRow(new Object[]{
                    "Tiền thuê phòng (" + p.getLoaiPhong() + ")",
                    vndFormat.format(giaPhong),
                    vndFormat.format(giaPhong)
            });
            tongTien += giaPhong;

            // 2. Load dịch vụ từ DB
            List<DichVu> allServices = dichVuRepo.getAll();
            for (DichVu dv : allServices) {
                if (dv.getMaPhong() != null && dv.getMaPhong().equals(p.getMaPhong())) {
                    invoiceModel.addRow(new Object[]{
                            "Dịch vụ: " + dv.getTenDV(),
                            vndFormat.format(dv.getGiaDV()),
                            vndFormat.format(dv.getGiaDV())
                    });
                    tongTien += dv.getGiaDV();
                    dichVuCuaPhong.add(dv); // Lưu lại để lát nữa xóa
                }
            }

            lblTongTien.setText("Tổng cộng: " + vndFormat.format(tongTien));
        }
    }

    private void xuLyThanhToan() {
        Phong p = (Phong) cboPhong.getSelectedItem();
        if (p == null) return;

        // Lấy tổng tiền từ nhãn (đã bao gồm phòng + dịch vụ)
        String tongTienText = lblTongTien.getText().replace("Tổng cộng: ", "").replace(" VND", "").replace(".", "").trim();
        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(tongTienText);
        } catch (NumberFormatException e) {
            tongTien = p.getGiaPhong(); // Fallback nếu lỗi
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán và trả phòng " + p.getMaPhong() + "?\n" +
                        "Tổng thực thu: " + vndFormat.format(tongTien),
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // --- (MỚI) BƯỚC 1: TẠO CHUỖI CHI TIẾT DỊCH VỤ ---
            StringBuilder sbDichVu = new StringBuilder();
            if (dichVuCuaPhong.isEmpty()) {
                sbDichVu.append("Không sử dụng dịch vụ");
            } else {
                for (DichVu dv : dichVuCuaPhong) {
                    sbDichVu.append(dv.getTenDV()).append(", ");
                }
                // Xóa dấu phẩy thừa ở cuối
                if (sbDichVu.length() > 2) {
                    sbDichVu.setLength(sbDichVu.length() - 2);
                }
            }

            // --- (MỚI) BƯỚC 2: GHI NHẬN VÀO LỊCH SỬ (Gửi kèm danh sách dịch vụ) ---
            String msg = ThanhToan.ghiNhanThanhToan(p, tongTien, sbDichVu.toString());

            // BƯỚC 3: CẬP NHẬT TRẠNG THÁI PHÒNG
            p.setTrangThai(false);
            p.setKhachThue(null);
            if (!phongRepo.update(p)) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật Phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // BƯỚC 4: XÓA DỊCH VỤ KHỎI DB (Để dọn phòng cho khách mới)
            // Dữ liệu đã được lưu an toàn trong ThanhToan.java ở Bước 2 rồi
            for (DichVu dv : dichVuCuaPhong) {
                dichVuRepo.delete(String.valueOf(dv.getMaDV()));
            }

            JOptionPane.showMessageDialog(this, msg);

            // Refresh lại giao diện
            loadOccupiedRooms();
            invoiceModel.setRowCount(0);
            lblTongTien.setText("Tổng cộng: 0 VND");
        }
    }
}