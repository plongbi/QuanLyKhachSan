package model;

import java.text.DecimalFormat;

public class Phong {
    private String maPhong;
    private String loaiPhong;
    private double giaPhong;
    private boolean trangThai;
    private KhachHang KhachThue;

    private static final DecimalFormat df = new DecimalFormat("#,###");

    public Phong(String maPhong, String loaiPhong, double giaPhong, boolean trangThai) {
        setMaPhong(maPhong);
        setLoaiPhong(loaiPhong);
        setGiaPhong(giaPhong);
        setTrangThai(trangThai);
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        if (giaPhong > 0) {
            this.giaPhong = giaPhong;
        }
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachThue() {
        return KhachThue;
    }

    public void setKhachThue(KhachHang khach) {
        this.KhachThue = khach;
    }

    // ===== ĐẶT PHÒNG =====
    public boolean datPhong(KhachHang khach) {
        if (trangThai) {
            System.out.println("Phong " + maPhong + " da co nguoi thue!");
            return false;
        }
        this.trangThai = true;
        this.KhachThue = khach;
        System.out.println("Phong " + maPhong + " da duoc dat cho khach: " + khach.getTen());
        return true;
    }

    // ===== TRẢ PHÒNG =====
    public boolean traPhong() {
        if (!trangThai) {
            System.out.println("Phong " + maPhong + " dang trong!");
            return false;
        }
        this.trangThai = false;
        System.out.println("Phong " + maPhong + " da duoc tra.");
        this.KhachThue = null;
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "Phong %s | Loai: %s | Gia: %s VND | Trang thai: %s",
                maPhong, loaiPhong, df.format(giaPhong),
                (trangThai ? "Da thue" : "Trong")
        );
    }
}