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
    private static Scanner sc = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#,###");

    private String maPhong;
    private String tenKhach;
    private double soTien;
    private String thoiGian;

    public ThanhToan(String maPhong, String tenKhach, double soTien) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.thoiGian = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // ===== GHI NHẬN THANH TOÁN =====
    public static void ghiNhanThanhToan(Phong phong){
        if(phong == null || phong.getKhachThue() == null){
            System.out.println("Khong the thanh toan! Phong va khach khong hop le!");
            return;
        }

        double soTien = phong.getGiaPhong();
        ThanhToan tt = new ThanhToan(
                phong.getMaPhong(),
                phong.getKhachThue().getTen(),
                soTien
        );

        lichSuThanhToan.add(tt);
        tongDoanhThu += soTien;
        System.out.println("Thanh toan thanh cong! So tien: " + df.format(soTien) + " VND");
    }

    // ===== THỰC HIỆN THANH TOÁN (khi trả phòng) =====
    public static void thanhToanPhong() {
        System.out.print("Nhap ma phong can thanh toan: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        if (!phong.isTrangThai()) {
            System.out.println("Phong nay hien dang trong, khong can thanh toan!");
            return;
        }

        ghiNhanThanhToan(phong);
        phong.traPhong();
    }

    // ===== XEM LỊCH SỬ THANH TOÁN =====
    public static void xemLichSuThanhToan(){
        if(lichSuThanhToan.isEmpty()){
            System.out.println("Chua co thanh toan nao!");
            return;
        }
        System.out.println("\n===== LICH SU THANH TOAN =====");
        for (ThanhToan tt : lichSuThanhToan) {
            System.out.println(tt);
        }
        System.out.println("================================");
        System.out.println("Tong doanh thu: " + df.format(tongDoanhThu) + " VND\n");
    }

    // ===== XEM DOANH THU =====
    public static void xemDoanhThu() {
        System.out.println("\n===== DOANH THU HIEN TAI =====");
        System.out.println("Tong doanh thu: " + df.format(tongDoanhThu) + " VND");
        System.out.println("================================\n");
    }

    // ===== TÌM PHÒNG THEO MÃ =====
    private static Phong timPhongTheoMa(String maPhong) {
        try {
            java.lang.reflect.Field field = QuanLyPhong.class.getDeclaredField("dsPhong");
            field.setAccessible(true);
            List<Phong> dsPhong = (List<Phong>) field.get(null);

            for (Phong p : dsPhong) {
                if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
                    return p;
                }
            }
        } catch (Exception e) {
            System.out.println("Loi khi tim phong: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "Phong: %s | Khach: %s | Tien: %s VND | Thoi gian: %s",
                maPhong, tenKhach, df.format(soTien), thoiGian
        );
    }

    public static double getTongDoanhThu() {
        return tongDoanhThu;
    }
}