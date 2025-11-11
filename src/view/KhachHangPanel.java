package view; // Phải cùng package với MainForm

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.KhachHang; // Import model KhachHang
import repository.IKhachHangRepository; // Import repository

public class KhachHangPanel extends JPanel {

    private IKhachHangRepository khachHangRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaKH, txtTen, txtCMND, txtSDT, txtEmail;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // Màu sắc
    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_HEADER = MainForm.COLOR_HEADER;
    private static final Color COLOR_TEXT = MainForm.COLOR_TEXT;

    public KhachHangPanel(IKhachHangRepository repo) {
        this.khachHangRepo = repo;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        initActionListeners();
        loadCustomerData();
    }

    private void initComponents() {
        // --- Panel Form (NORTH) ---
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        // Tạo các fields
        txtMaKH = new JTextField();
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        // Style cho các fields và labels
        fieldsPanel.add(CustomStyler.createStyledLabel("Mã KH:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtMaKH));
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Khách Hàng:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));
        fieldsPanel.add(CustomStyler.createStyledLabel("Email:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtEmail));

        txtMaKH.setEditable(false); // Mã KH không nên sửa, nó được tạo tự động hoặc dùng để chọn
        txtMaKH.setText("Tự động hoặc chọn từ bảng");

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
        String[] columnNames = { "Mã KH", "Tên", "CMND", "SĐT", "Email" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        table = new JTable(model);
        CustomStyler.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Thêm các components vào panel chính
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        // Nút Thêm
        btnThem.addActionListener(e -> themKhachHang());

        // Nút Sửa
        btnSua.addActionListener(e -> suaKhachHang());

        // Nút Xóa
        btnXoa.addActionListener(e -> xoaKhachHang());

        // Nút Làm Mới
        btnLamMoi.addActionListener(e -> clearForm());

        // Sự kiện click vào 1 dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtMaKH.setText(model.getValueAt(selectedRow, 0).toString());
                txtTen.setText(model.getValueAt(selectedRow, 1).toString());
                txtCMND.setText(model.getValueAt(selectedRow, 2).toString());
                txtSDT.setText(model.getValueAt(selectedRow, 3).toString());
                txtEmail.setText(model.getValueAt(selectedRow, 4).toString());

                txtMaKH.setEditable(false); // Không cho sửa Mã KH
            }
        });
    }

    /**
     * Tải dữ liệu từ Repository lên JTable
     */
    private void loadCustomerData() {
        model.setRowCount(0); // Xóa hết dữ liệu cũ
        List<KhachHang> customers = khachHangRepo.getAll();
        for (KhachHang kh : customers) {
            model.addRow(new Object[] {
                    kh.getMaID(),
                    kh.getTen(),
                    kh.getSoCMND(),
                    kh.getSoDienThoai(),
                    kh.getEmail()
            });
        }
    }

    /**
     * Chức năng Thêm Khách Hàng
     */
    private void themKhachHang() {
        String ten = txtTen.getText();
        String cmnd = txtCMND.getText();
        String sdt = txtSDT.getText();
        String email = txtEmail.getText();

        if (ten.isEmpty() || cmnd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và CMND không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dùng constructor tạo ID tự động
        KhachHang kh = new KhachHang(ten, cmnd, sdt, email);

        if (khachHangRepo.add(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công! ID mới: " + kh.getMaID());
            loadCustomerData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Sửa Khách Hàng
     */
    private void suaKhachHang() {
        String maKH = txtMaKH.getText();
        if (maKH.isEmpty() || maKH.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng để sửa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dùng constructor rỗng và setter
        KhachHang kh = new KhachHang();
        kh.setMaID(maKH);
        kh.setTen(txtTen.getText());
        kh.setSoCMND(txtCMND.getText());
        kh.setSoDienThoai(txtSDT.getText());
        kh.setEmail(txtEmail.getText());

        if (khachHangRepo.update(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadCustomerData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Xóa Khách Hàng
     */
    private void xoaKhachHang() {
        String maKH = txtMaKH.getText();
        if (maKH.isEmpty() || maKH.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng " + maKH + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangRepo.delete(maKH)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadCustomerData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Dọn dẹp form nhập liệu
     */
    private void clearForm() {
        txtMaKH.setText("Tự động hoặc chọn từ bảng");
        txtMaKH.setEditable(false);
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}