package view; // Phải cùng package với MainForm và KhachHangPanel

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// CÁC IMPORT MỚI ĐỂ FORMAT SỐ
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import model.NhanVien; // Import model NhanVien
import repository.INhanVienRepository; // Import repository NhanVien

public class NhanVienPanel extends JPanel {

    private INhanVienRepository nhanVienRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaNV, txtTen, txtCMND, txtSDT, txtChucVu, txtLuong;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // (MỚI) Đối tượng để format tiền tệ
    private DecimalFormat vndFormat;

    // Màu sắc
    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_HEADER = MainForm.COLOR_HEADER;
    private static final Color COLOR_TEXT = MainForm.COLOR_TEXT;

    public NhanVienPanel(INhanVienRepository repo) {
        this.nhanVienRepo = repo;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // (MỚI) Khởi tạo formatter kiểu Việt Nam (100.000VND)
        // Dùng mẫu "###,###" nhưng thay dấu , bằng dấu .
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm
        vndFormat = new DecimalFormat("###,### VND", symbols);

        // (Code cũ)
        initComponents();
        initActionListeners();
        loadNhanVienData();
    }

    private void initComponents() {
        // --- Panel Form (NORTH) ---
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        // Tạo các fields
        txtMaNV = new JTextField();
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtChucVu = new JTextField();
        txtLuong = new JTextField();

        // Style cho các fields và labels
        fieldsPanel.add(CustomStyler.createStyledLabel("Mã NV:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtMaNV));
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Nhân Viên:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));
        fieldsPanel.add(CustomStyler.createStyledLabel("Chức Vụ:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtChucVu));
        fieldsPanel.add(CustomStyler.createStyledLabel("Lương:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtLuong));

        txtMaNV.setEditable(false);
        txtMaNV.setText("Tự động hoặc chọn từ bảng");

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        btnThem = CustomStyler.createStyledButton("Thêm Mới");
        btnSua = CustomStyler.createStyledButton("Cập Nhật");
        btnXoa = CustomStyler.createStyledButton("Xóa");
        btnLamMoi = CustomStyler.createStyledButton("Làm Mới Form");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Panel Bảng (CENTER) ---
        String[] columnNames = { "Mã NV", "Tên", "CMND", "SĐT", "Chức Vụ", "Lương" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        CustomStyler.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnLamMoi.addActionListener(e -> clearForm());

        // Sự kiện click vào 1 dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtMaNV.setText(model.getValueAt(selectedRow, 0).toString());
                txtTen.setText(model.getValueAt(selectedRow, 1).toString());
                txtCMND.setText(model.getValueAt(selectedRow, 2).toString());
                txtSDT.setText(model.getValueAt(selectedRow, 3).toString());
                txtChucVu.setText(model.getValueAt(selectedRow, 4).toString());

                // ===== SỬA ĐỔI QUAN TRỌNG Ở ĐÂY =====
                // Lấy chuỗi đã định dạng từ bảng (ví dụ: "100.000 VND")
                String luongFormatted = model.getValueAt(selectedRow, 5).toString();

                // Chuyển nó về dạng số (ví dụ: "100000")
                String luongUnformatted = luongFormatted
                        .replace("VND", "")  // Xóa chữ VND
                        .replace(".", "")    // Xóa dấu chấm
                        .trim();             // Xóa khoảng trắng

                // Đặt số đã "lột" vào ô text
                txtLuong.setText(luongUnformatted);
                // ===================================

                txtMaNV.setEditable(false);
            }
        });
    }

    /**
     * Tải dữ liệu từ Repository lên JTable
     */
    private void loadNhanVienData() {
        model.setRowCount(0);
        List<NhanVien> employees = nhanVienRepo.getAll();
        for (NhanVien nv : employees) {
            model.addRow(new Object[] {
                    nv.getMaID(),
                    nv.getTen(),
                    nv.getSoCMND(),
                    nv.getSoDienThoai(),
                    nv.getChucVu(),
                    // ===== SỬA ĐỔI QUAN TRỌNG Ở ĐÂY =====
                    vndFormat.format(nv.getLuongCoBan()) // Bọc trong hàm format
                    // ===================================
            });
        }
    }

    /**
     * Chức năng Thêm Nhân Viên (Không thay đổi)
     */
    private void themNhanVien() {
        try {
            String ten = txtTen.getText();
            String cmnd = txtCMND.getText();
            String sdt = txtSDT.getText();
            String chucVu = txtChucVu.getText();
            String luongStr = txtLuong.getText(); // Đọc từ ô text (đã là số)

            if (ten.isEmpty() || cmnd.isEmpty() || chucVu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên, CMND và Chức Vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double luong = Double.parseDouble(luongStr);

            NhanVien nv = new NhanVien(ten, cmnd, sdt, luong, chucVu);

            if (nhanVienRepo.add(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! ID mới: " + nv.getMaID());
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là một con số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Sửa Nhân Viên (Không thay đổi)
     */
    private void suaNhanVien() {
        String maNV = txtMaNV.getText();
        if (maNV.isEmpty() || maNV.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên từ bảng để sửa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double luong = Double.parseDouble(txtLuong.getText()); // Đọc từ ô text (đã là số)

            NhanVien nv = new NhanVien(); // Cần constructor rỗng
            nv.setMaID(maNV);
            nv.setTen(txtTen.getText());
            nv.setSoCMND(txtCMND.getText());
            nv.setSoDienThoai(txtSDT.getText());
            nv.setChucVu(txtChucVu.getText());
            nv.setLuongCoBan(luong); // Dùng setLuongCoBan()

            if (nhanVienRepo.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là một con số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Xóa Nhân Viên (Không thay đổi)
     */
    private void xoaNhanVien() {
        String maNV = txtMaNV.getText();
        if (maNV.isEmpty() || maNV.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên từ bảng để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nhân viên " + maNV + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nhanVienRepo.delete(maNV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Dọn dẹp form nhập liệu (Không thay đổi)
     */
    private void clearForm() {
        txtMaNV.setText("Tự động hoặc chọn từ bảng");
        txtMaNV.setEditable(false);
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtChucVu.setText("");
        txtLuong.setText("");
        table.clearSelection();
    }
}