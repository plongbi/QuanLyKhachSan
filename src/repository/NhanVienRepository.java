package repository;

import model.NhanVien; // Import model NhanVien
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienRepository implements INhanVienRepository {

    // Ánh xạ ResultSet sang đối tượng NhanVien
    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        // 1. Dùng constructor rỗng (bạn vừa thêm ở bước 1)
        NhanVien nv = new NhanVien();

        // 2. Dùng các hàm setter để nạp dữ liệu
        // GIẢ ĐỊNH bạn có hàm setMaID() trong lớp Person
        nv.setMaID(rs.getString("MaNV"));
        nv.setTen(rs.getString("Ten"));
        nv.setSoCMND(rs.getString("SoCMND"));
        nv.setSoDienThoai(rs.getString("SoDienThoai"));

        // KHỚP VỚI MODEL CỦA BẠN:
        nv.setChucVu(rs.getString("ChucVu"));
        nv.setLuongCoBan(rs.getDouble("LuongCoBan")); // Dùng setLuongCoBan

        return nv;
    }

    @Override
    public List<NhanVien> getAll() {
        List<NhanVien> dsNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsNhanVien.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNhanVien;
    }

    @Override
    public NhanVien getByName(String ten) {
        String sql = "SELECT * FROM nhanvien WHERE Ten LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ten + "%"); // Thêm % để tìm kiếm gần đúng
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(NhanVien nv) {
        // KHỚP VỚI MODEL CỦA BẠN: Dùng cột ChucVu, LuongCoBan
        String sql = "INSERT INTO nhanvien (MaNV, Ten, SoCMND, SoDienThoai, ChucVu, LuongCoBan) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaID());
            ps.setString(2, nv.getTen());
            ps.setString(3, nv.getSoCMND());
            ps.setString(4, nv.getSoDienThoai());

            // KHỚP VỚI MODEL CỦA BẠN:
            ps.setString(5, nv.getChucVu());     // Dùng getChucVu
            ps.setDouble(6, nv.getLuongCoBan()); // Dùng getLuongCoBan

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NhanVien nv) {
        // KHỚP VỚI MODEL CỦA BẠN:
        String sql = "UPDATE nhanvien SET Ten = ?, SoCMND = ?, SoDienThoai = ?, ChucVu = ?, LuongCoBan = ? WHERE MaNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTen());
            ps.setString(2, nv.getSoCMND());
            ps.setString(3, nv.getSoDienThoai());

            // KHỚP VỚI MODEL CỦA BẠN:
            ps.setString(4, nv.getChucVu());     // Dùng getChucVu
            ps.setDouble(5, nv.getLuongCoBan()); // Dùng getLuongCoBan

            ps.setString(6, nv.getMaID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM nhanvien WHERE MaNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public NhanVien getById(String id) { // Sửa: Kiểu trả về là NhanVien
        String sql = "SELECT * FROM nhanvien WHERE MaNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs); // Trả về NhanVien
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}