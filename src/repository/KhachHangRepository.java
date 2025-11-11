package repository;

import model.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangRepository implements IKhachHangRepository {

    // Phương thức này ánh xạ dữ liệu từ ResultSet (CSDL) sang đối tượng Java
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        // 1. Dùng constructor rỗng để tạo đối tượng
        KhachHang kh = new KhachHang();

        // 2. Dùng các hàm setter (bao gồm cả setMaID mới) để nạp dữ liệu
        kh.setMaID(rs.getString("MaKH"));
        kh.setTen(rs.getString("Ten"));
        kh.setSoCMND(rs.getString("SoCMND"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setEmail(rs.getString("Email"));

        return kh;
    }

    @Override
    public List<KhachHang> getAll() {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM khachhang"; //

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Sử dụng hàm mapResultSetToKhachHang
                dsKhachHang.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }

    @Override
    public KhachHang getByName(String ten) {
        String sql = "SELECT * FROM khachhang WHERE Ten like ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(KhachHang kh) {
        String sql = "INSERT INTO khachhang (MaKH, Ten, SoCMND, SoDienThoai, Email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Lấy MaID đã được tạo bởi constructor của KhachHang
            ps.setString(1, kh.getMaID());
            ps.setString(2, kh.getTen());
            ps.setString(3, kh.getSoCMND());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(KhachHang kh) {
        String sql = "UPDATE khachhang SET Ten = ?, SoCMND = ?, SoDienThoai = ?, Email = ? WHERE MaKH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTen());
            ps.setString(2, kh.getSoCMND());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getMaID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM khachhang WHERE MaKH = ?";

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
    public KhachHang getById(String id) {
        String sql = "SELECT * FROM khachhang WHERE MaKH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}