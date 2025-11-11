// Đặt file này trong package 'view'
package view;

// Các import cần thiết cho Java Swing
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Import Model và Repository của BẠN
import model.KhachHang; // Import lớp KhachHang của bạn
import repository.IKhachHangRepository;

public class KhachHangView extends JFrame {

    // 1. Thành phần UI
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTen, txtSdt, txtCmnd, txtEmail; // THAY ĐỔI: Thêm txtEmail
    private JButton btnThem, btnXoa;

    // 2. Liên kết tới Repository
    private IKhachHangRepository khachHangRepository;

    public KhachHangView(IKhachHangRepository repository) {
        this.khachHangRepository = repository;

        // --- Cài đặt cửa sổ chính ---
        setTitle("Quản Lý Khách Hàng");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Panel Nhập liệu (NORTH) ---
        // THAY ĐỔI: Tăng GridLayout từ 4 lên 5 để chứa Email
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        inputPanel.add(new JLabel("Tên Khách Hàng:"));
        txtTen = new JTextField();
        inputPanel.add(txtTen);

        inputPanel.add(new JLabel("Số Điện Thoại:"));
        txtSdt = new JTextField();
        inputPanel.add(txtSdt);

        inputPanel.add(new JLabel("CMND/CCCD:"));
        txtCmnd = new JTextField();
        inputPanel.add(txtCmnd);

        // THAY ĐỔI: Thêm trường nhập Email
        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        add(inputPanel, BorderLayout.NORTH);

        // --- 2. Bảng Hiển thị Dữ liệu (CENTER) ---
        // THAY ĐỔI: Thêm cột "Email" vào bảng
        String[] columnNames = {"ID", "Tên Khách Hàng", "SĐT", "CMND", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. Panel Nút bấm (SOUTH) ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- 4. Thêm sự kiện cho nút bấm ---
        btnThem.addActionListener(e -> themKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());

        // --- 5. Tải dữ liệu ban đầu ---
        loadTableData();
    }

    /**
     * Tải/Làm mới dữ liệu từ Repository lên JTable
     */
    private void loadTableData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<KhachHang> danhSach = khachHangRepository.getAll();

        // Duyệt danh sách và thêm hàng
        for (KhachHang kh : danhSach) {
            // !! THAY ĐỔI QUAN TRỌNG !!
            // Sử dụng đúng tên hàm getter từ file KhachHang.java của bạn
            Object[] row = {
                    kh.getMaID(),       // Dùng getMaID()
                    kh.getTen(),
                    kh.getSoDienThoai(), // Dùng getSoDienThoai()
                    kh.getSoCMND(),     // Dùng getSoCMND()
                    kh.getEmail()       // Thêm getEmail()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Thêm"
     */
    private void themKhachHang() {
        try {
            String ten = txtTen.getText();
            String sdt = txtSdt.getText();
            String cmnd = txtCmnd.getText();
            String email = txtEmail.getText(); // THAY ĐỔI: Lấy email từ text field

            // THAY ĐỔI: Thêm email vào kiểm tra
            if (ten.isEmpty() || sdt.isEmpty() || cmnd.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // !! THAY ĐỔI QUAN TRỌNG !!
            // Tạo đối tượng KhachHang bằng constructor đầy đủ của bạn
            KhachHang kh = new KhachHang(ten, cmnd, sdt, email);

            // Gọi hàm add() từ repository (KHÔNG gọi hàm static trong model)
            khachHangRepository.add(kh);

            loadTableData(); // Tải lại bảng

            // Xóa trống các ô nhập liệu
            txtTen.setText("");
            txtSdt.setText("");
            txtCmnd.setText("");
            txtEmail.setText(""); // THAY ĐỔI: Xóa ô email

            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Xóa"
     */
    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // !! THAY ĐỔI QUAN TRỌNG !!
            // ID bây giờ là String (từ getMaID()), không phải int
            String id = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa khách hàng có ID = " + id + "?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi hàm delete() từ repository
                khachHangRepository.delete(id); // Giả sử hàm delete của bạn nhận String ID

                loadTableData();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}