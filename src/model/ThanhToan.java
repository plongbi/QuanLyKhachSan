package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;

public class ThanhToan {
    private static List<ThanhToan> lichSuThanhToan = new ArrayList<>();
    private static double tongDoanhThu = 0;
    private static final DecimalFormat df = new DecimalFormat("#,###");

    private String maPhong;
    private String tenKhach;
    private double soTien;
    private String thoiGian;
    private String chiTietDichVu; // (MỚI) Biến lưu danh sách dịch vụ

    // Constructor cập nhật
    public ThanhToan(String maPhong, String tenKhach, double soTien, String chiTietDichVu) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.chiTietDichVu = chiTietDichVu; // (MỚI)
        this.thoiGian = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // Hàm cũ (giữ lại để tương thích với Main.java console nếu cần)
    public static String ghiNhanThanhToan(Phong phong) {
        return ghiNhanThanhToan(phong, 0, "Không có dịch vụ");
    }

    // (MỚI) Hàm ghi nhận thanh toán có thêm Dịch vụ & Tổng tiền thực tế
    public static String ghiNhanThanhToan(Phong phong, double tongTienThucTe, String dsDichVu) {
        if(phong == null || phong.getKhachThue() == null){
            return "Không thể thanh toán! Phòng và khách không hợp lệ!";
        }

        // Nếu tổng tiền thực tế = 0 (chưa tính) thì lấy giá phòng cơ bản
        double soTienCuoi = (tongTienThucTe > 0) ? tongTienThucTe : phong.getGiaPhong();

        ThanhToan tt = new ThanhToan(
                phong.getMaPhong(),
                phong.getKhachThue().getTen(),
                soTienCuoi,
                dsDichVu // (MỚI) Lưu chuỗi dịch vụ vào lịch sử
        );

        lichSuThanhToan.add(tt);
        tongDoanhThu += soTienCuoi;

        return "Thanh toán thành công! Tổng tiền: " + df.format(soTienCuoi) + " VND";
    }

    public static void thanhToanPhong() {
        // (Code console giữ nguyên, không ảnh hưởng)
        System.out.println("Chức năng này trên Console chưa hỗ trợ chi tiết dịch vụ.");
    }

    public static void xemLichSuThanhToan(){
        if(lichSuThanhToan.isEmpty()){
            System.out.println("Chưa có thanh toán nào!");
            return;
        }
        System.out.println("\n===== LỊCH SỬ THANH TOÁN =====");
        for (ThanhToan tt : lichSuThanhToan) {
            System.out.println(tt);
        }
        System.out.println("================================");
    }

    public static void xemDoanhThu() {
        System.out.println("Tổng doanh thu: " + df.format(tongDoanhThu) + " VND");
    }

    @Override
    public String toString() {
        // (MỚI) Cập nhật hiển thị có thêm chi tiết dịch vụ
        return String.format(
                "Phòng: %s | Khach: %s | Tiền: %s VND | Ngày: %s\n   -> Dịch vụ: %s",
                maPhong, tenKhach, df.format(soTien), thoiGian, chiTietDichVu
        );
    }
}