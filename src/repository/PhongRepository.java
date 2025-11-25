// Tệp: src/repository/PhongRepository.java
package repository;

import model.KhachHang;
import model.Phong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongRepository implements IPhongRepository {

    // (MỚI) Không cần IKhachHangRepository nữa
    // private IKhachHangRepository khachHangRepo;

    // (MỚI) Dùng constructor rỗng
    public PhongRepository() {
        // Không cần khởi tạo repo khác
    }

    private Phong mapResultSetToPhong(ResultSet rs) throws SQLException {
        Phong p = new Phong();
        p.setMaPhong(rs.getString("MaPhong"));
        p.setLoaiPhong(rs.getString("LoaiPhong"));
        p.setGiaPhong(rs.getDouble("GiaPhong"));
        p.setTrangThai(rs.getBoolean("TrangThai"));

        // (MỚI) Lấy thông tin KhachHang từ JOIN
        String maKH = rs.getString("MaKH");
        if (maKH != null) {
            // Tạo đối tượng KhachHang từ các cột đã JOIN
            KhachHang kh = new KhachHang();
            kh.setMaID(maKH);
            kh.setTen(rs.getString("Ten")); // Lấy từ bảng khachhang
            kh.setSoCMND(rs.getString("SoCMND")); // Lấy từ bảng khachhang
            kh.setSoDienThoai(rs.getString("SoDienThoai")); // Lấy từ bảng khachhang
            kh.setEmail(rs.getString("Email")); // Lấy từ bảng khachhang

            p.setKhachThue(kh);
        }
        return p;
    }

    // --- HÀM MỚI: Tự động tạo phòng ảo "MENU" để chứa dịch vụ gốc ---
    public void khoiTaoPhongChoMenu() {
        // 1. Kiểm tra xem phòng MENU đã tồn tại chưa
        String checkSql = "SELECT MaPhong FROM phong WHERE MaPhong = 'MENU'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql);
             ResultSet rs = psCheck.executeQuery()) {

            if (rs.next()) {
                // Đã có phòng MENU rồi thì không làm gì cả
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Nếu chưa có, tiến hành tạo mới
        String insertSql = "INSERT INTO phong (MaPhong, LoaiPhong, GiaPhong, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

            psInsert.setString(1, "MENU");
            psInsert.setString(2, "KHO_DICH_VU"); // Loại phòng
            psInsert.setDouble(3, 0);             // Giá 0
            psInsert.setBoolean(4, false);        // Trạng thái trống

            psInsert.executeUpdate();
            System.out.println("Đã tự động khởi tạo phòng 'MENU' thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi khi khởi tạo phòng MENU: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public List<Phong> getAll() {
        List<Phong> dsPhong = new ArrayList<>();
        // (MỚI) Dùng LEFT JOIN
        String sql = "SELECT p.*, kh.Ten, kh.SoCMND, kh.SoDienThoai, kh.Email " +
                "FROM phong p " +
                "LEFT JOIN khachhang kh ON p.MaKH = kh.MaKH";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // mapResultSetToPhong bây giờ đã bao gồm cả việc map KhachHang
                dsPhong.add(mapResultSetToPhong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }

    @Override
    public Phong getById(String id) {
        // (MỚI) Dùng LEFT JOIN
        String sql = "SELECT p.*, kh.Ten, kh.SoCMND, kh.SoDienThoai, kh.Email " +
                "FROM phong p " +
                "LEFT JOIN khachhang kh ON p.MaKH = kh.MaKH " +
                "WHERE p.MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPhong(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ... (Hàm add, update, delete giữ nguyên như phiên bản trước) ...

    @Override
    public boolean add(Phong p) {
        String sql = "INSERT INTO phong (MaPhong, LoaiPhong, GiaPhong, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getLoaiPhong());
            ps.setDouble(3, p.getGiaPhong());
            ps.setBoolean(4, false); // Mặc định là trống

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Phong p) {
        String sql = "UPDATE phong SET LoaiPhong = ?, GiaPhong = ?, TrangThai = ?, MaKH = ? WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getLoaiPhong());
            ps.setDouble(2, p.getGiaPhong());
            ps.setBoolean(3, p.isTrangThai());

            if (p.isTrangThai() && p.getKhachThue() != null) {
                ps.setString(4, p.getKhachThue().getMaID());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            ps.setString(5, p.getMaPhong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM phong WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}