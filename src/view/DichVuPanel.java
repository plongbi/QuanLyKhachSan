package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.DichVu;
import repository.IDichVuRepository;

public class DichVuPanel extends JPanel {

    private IDichVuRepository dichVuRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaDV, txtTenDV, txtGia; // Đã bỏ txtMaPhong
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private DecimalFormat vndFormat;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;

    public DichVuPanel(IDichVuRepository repo) {
        this.dichVuRepo = repo;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,###", symbols);

        initComponents();
        initActionListeners();
        loadData();
    }

    private void initComponents() {
        // --- Panel Form ---
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        // Giảm xuống còn 3 hàng vì không nhập Mã Phòng nữa
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        txtMaDV = new JTextField();
        txtTenDV = new JTextField();
        txtGia = new JTextField();

        // Hàng 1
        fieldsPanel.add(CustomStyler.createStyledLabel("Mã Dịch Vụ (Auto):"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtMaDV));

        // Hàng 2
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Dịch Vụ (Menu):"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTenDV));

        // Hàng 3
        fieldsPanel.add(CustomStyler.createStyledLabel("Đơn Giá (VNĐ):"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtGia));

        txtMaDV.setEditable(false);
        txtMaDV.setText("Tự động tăng");
        txtMaDV.setBackground(new Color(240, 240, 240));

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        btnThem = CustomStyler.createStyledButton("Thêm Mới");
        btnSua = CustomStyler.createStyledButton("Cập Nhật");
        btnXoa = CustomStyler.createStyledButton("Xóa");
        btnLamMoi = CustomStyler.createStyledButton("Làm Mới");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Bảng dữ liệu ---
        // Bảng chỉ hiện Tên và Giá (Ẩn cột Mã Phòng đi vì mặc định là Menu)
        String[] columnNames = { "Mã DV", "Tên Dịch Vụ", "Đơn Giá Niêm Yết" };
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
        btnThem.addActionListener(e -> themDichVu());
        btnSua.addActionListener(e -> suaDichVu());
        btnXoa.addActionListener(e -> xoaDichVu());
        btnLamMoi.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtMaDV.setText(model.getValueAt(row, 0).toString());
                txtTenDV.setText(model.getValueAt(row, 1).toString());
                String giaStr = model.getValueAt(row, 2).toString().replace(".", "");
                txtGia.setText(giaStr);
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<DichVu> list = dichVuRepo.getAll();
        for (DichVu dv : list) {
            // QUAN TRỌNG: Chỉ hiển thị những dòng là MENU
            // (Bỏ qua những dòng dịch vụ lẻ tẻ của từng phòng)
            if (dv.getMaPhong() != null && dv.getMaPhong().equalsIgnoreCase("MENU")) {
                model.addRow(new Object[]{
                        dv.getMaDV(),
                        dv.getTenDV(),
                        vndFormat.format(dv.getGiaDV())
                });
            }
        }
    }

    private void themDichVu() {
        try {
            String ten = txtTenDV.getText();
            String giaText = txtGia.getText();

            if (ten.isEmpty() || giaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và giá!");
                return;
            }

            double gia = Double.parseDouble(giaText);

            // QUAN TRỌNG: Tự động gán MaPhong = "MENU"
            DichVu dv = new DichVu(ten, gia, "MENU");

            if (dichVuRepo.add(dv)) {
                JOptionPane.showMessageDialog(this, "Thêm vào Menu thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số!");
        }
    }

    private void suaDichVu() {
        String idStr = txtMaDV.getText();
        if (idStr.isEmpty() || !idStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ từ bảng để sửa!");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String ten = txtTenDV.getText();
            double gia = Double.parseDouble(txtGia.getText());

            // Vẫn giữ nguyên MaPhong = "MENU" khi sửa
            DichVu dv = new DichVu(id, ten, gia, "MENU");

            if (dichVuRepo.update(dv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật Menu thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void xoaDichVu() {
        String idStr = txtMaDV.getText();
        if (idStr.isEmpty() || !idStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa món này khỏi Menu? (ID: " + idStr + ")",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dichVuRepo.delete(idStr)) {
                JOptionPane.showMessageDialog(this, "Đã xóa khỏi Menu!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void clearForm() {
        txtMaDV.setText("Tự động tăng");
        txtTenDV.setText("");
        txtGia.setText("");
        table.clearSelection();
    }
}