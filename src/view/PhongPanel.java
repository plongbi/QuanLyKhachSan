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

    private IPhongRepository phongRepo;
    private IKhachHangRepository khachHangRepo;
    private IDichVuRepository dichVuRepo;

    private JPanel roomCardsPanel;
    private DecimalFormat vndFormat;

    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_AVAILABLE_BG = new Color(240, 255, 240);
    private static final Color COLOR_RENTED_BG = new Color(255, 240, 240);
    private static final Color COLOR_BORDER = new Color(200, 200, 200);
    private static final Color COLOR_BUTTON_AVAILABLE = new Color(46, 204, 113);
    private static final Color COLOR_BUTTON_RENTED = new Color(52, 152, 219);

    public PhongPanel(IPhongRepository phongRepo, IKhachHangRepository khachHangRepo, IDichVuRepository dichVuRepo) {
        this.phongRepo = phongRepo;
        this.khachHangRepo = khachHangRepo;
        this.dichVuRepo = dichVuRepo;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        loadPhongCards();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Sơ Đồ Phòng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(MainForm.COLOR_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton btnRefresh = CustomStyler.createStyledButton("Làm Mới");
        btnRefresh.setPreferredSize(new Dimension(120, 40));
        btnRefresh.addActionListener(e -> loadPhongCards());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnRefresh);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // GridLayout 5 cột, tự động xuống dòng
        roomCardsPanel = new JPanel(new GridLayout(0, 5, 15, 15));
        roomCardsPanel.setBackground(COLOR_BACKGROUND);

        // Wrapper để tránh GridLayout bị kéo giãn
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(COLOR_BACKGROUND);
        wrapperPanel.add(roomCardsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPhongCards() {
        roomCardsPanel.removeAll();
        List<Phong> danhSachPhong = phongRepo.getAll();

        if (danhSachPhong.isEmpty()) {
            roomCardsPanel.setLayout(new BorderLayout());
            JLabel lblTrong = new JLabel("Chưa có phòng nào trong hệ thống.", SwingConstants.CENTER);
            lblTrong.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            roomCardsPanel.add(lblTrong, BorderLayout.CENTER);
        } else {
            roomCardsPanel.setLayout(new GridLayout(0, 5, 15, 15));
            for (Phong phong : danhSachPhong) {
                // === QUAN TRỌNG: ẨN PHÒNG MENU ===
                if (phong.getMaPhong().equalsIgnoreCase("MENU")) {
                    continue;
                }

                JPanel card = createRoomCard(phong);
                roomCardsPanel.add(card);
            }
        }
        roomCardsPanel.revalidate();
        roomCardsPanel.repaint();
    }

    private JPanel createRoomCard(Phong phong) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 180));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblMaPhong = new JLabel(phong.getMaPhong());
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMaPhong.setForeground(new Color(50, 50, 50));

        JLabel lblStatus = new JLabel("", SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));

        header.add(lblMaPhong, BorderLayout.WEST);
        header.add(lblStatus, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblLoai = new JLabel(phong.getLoaiPhong());
        lblLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblGia = new JLabel(vndFormat.format(phong.getGiaPhong()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGia.setForeground(MainForm.COLOR_PRIMARY);

        JLabel lblKhach = new JLabel(" ");
        lblKhach.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        body.add(lblLoai);
        body.add(Box.createVerticalStrut(5));
        body.add(lblGia);
        body.add(Box.createVerticalStrut(5));
        body.add(lblKhach);

        card.add(body, BorderLayout.CENTER);

        JButton btnAction = new JButton();
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(btnAction);
        card.add(footer, BorderLayout.SOUTH);

        if (phong.isTrangThai()) {
            card.setBackground(COLOR_RENTED_BG);
            lblStatus.setText("Đang Thuê");
            lblStatus.setForeground(Color.RED);
            btnAction.setText("Chi Tiết");
            btnAction.setBackground(COLOR_BUTTON_RENTED);

            if (phong.getKhachThue() != null) {
                lblKhach.setText("Khách: " + phong.getKhachThue().getTen());
            } else {
                lblKhach.setText("Khách: [Không rõ]");
            }
            btnAction.addActionListener(e -> hienThiChiTietPhong(phong));
        } else {
            card.setBackground(COLOR_AVAILABLE_BG);
            lblStatus.setText("Trống");
            lblStatus.setForeground(COLOR_BUTTON_AVAILABLE);
            btnAction.setText("Đặt Phòng");
            btnAction.setBackground(COLOR_BUTTON_AVAILABLE);
            lblKhach.setText(" ");
            btnAction.addActionListener(e -> datPhong(phong));
        }
        return card;
    }

    private void hienThiChiTietPhong(Phong phong) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(600, 400));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Phòng & Khách Hàng"));

        infoPanel.add(new JLabel("Phòng: " + phong.getMaPhong() + " - Loại: " + phong.getLoaiPhong()));
        infoPanel.add(new JLabel("Giá phòng: " + vndFormat.format(phong.getGiaPhong()) + "/ngày"));

        String tenKhach = (phong.getKhachThue() != null) ? phong.getKhachThue().getTen() : "---";
        String cmnd = (phong.getKhachThue() != null) ? phong.getKhachThue().getSoCMND() : "---";
        infoPanel.add(new JLabel("Khách đang thuê: " + tenKhach));
        infoPanel.add(new JLabel("Số CMND: " + cmnd));

        panel.add(infoPanel, BorderLayout.NORTH);

        String[] cols = {"Tên Dịch Vụ", "Đơn Giá"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        CustomStyler.styleTable(table);

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

        JButton btnAddService = CustomStyler.createStyledButton("Thêm Dịch Vụ Mới");
        btnAddService.addActionListener(e -> themDichVuVaoPhong(phong, tableModel));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnAddService);
        panel.add(btnPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Chi Tiết Phòng " + phong.getMaPhong(), JOptionPane.PLAIN_MESSAGE);
    }

    private void themDichVuVaoPhong(Phong phong, DefaultTableModel tableModel) {
        List<DichVu> allServices = dichVuRepo.getAll();
        List<DichVu> menuList = new ArrayList<>();

        for (DichVu dv : allServices) {
            if (dv.getMaPhong() != null && dv.getMaPhong().equalsIgnoreCase("MENU")) {
                menuList.add(dv);
            }
        }

        if (menuList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dịch vụ!");
            return;
        }

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
                DichVu dichVuMoi = new DichVu(
                        selectedMenu.getTenDV(),
                        selectedMenu.getGiaDV(),
                        phong.getMaPhong()
                );

                if (dichVuRepo.add(dichVuMoi)) {
                    JOptionPane.showMessageDialog(this, "Đã thêm: " + dichVuMoi.getTenDV());
                    tableModel.addRow(new Object[]{dichVuMoi.getTenDV(), vndFormat.format(dichVuMoi.getGiaDV())});
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi thêm dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void datPhong(Phong phong) {
        List<KhachHang> dsKhach = khachHangRepo.getAll();
        if (dsKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có khách hàng nào! Hãy thêm khách trước.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
                phong.setTrangThai(true);
                phong.setKhachThue(khach);

                if (phongRepo.update(phong)) {
                    JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
                    loadPhongCards();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}