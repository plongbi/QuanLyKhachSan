package model;

import java.util.ArrayList;
import java.util.Scanner;

public class NhanVien extends Person {
    private double luongCoBan;
    private String chucvu;
    private static ArrayList<NhanVien> dsNV = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public NhanVien() {
        super(); // Gọi constructor rỗng của Person
    }
    // ===== CONSTRUCTOR =====
    public NhanVien(String ten, String soCMND, String soDienThoai, double luongCoBan,String chucvu) {
        super("NV",ten, soCMND, soDienThoai);
        setLuongCoBan(luongCoBan);
        setChucVu(chucvu);
    }

    // ===== GETTER & SETTER =====
    public double getLuongCoBan() {
        return luongCoBan;
    }
    public String getChucVu(){
        return chucvu;
    }

    public void setLuongCoBan(double luongCoBan) {
        if (luongCoBan >= 0) {
            this.luongCoBan = luongCoBan;
        }
    }
    public void setChucVu(String chucvu){
        this.chucvu = chucvu;
    }

    // ===== HIỂN THỊ THÔNG TIN =====
    @Override
    public String toString() {
        return String.format(
                "Nhan vien [ID=%s, Ten=%s, CMND=%s, SDT=%s, Luong=%.2f, Chucvu = %s]",
                getMaID(), getTen(), getSoCMND(), getSoDienThoai(), getLuongCoBan(),getChucVu()
        );
    }

    // ===== CÁC PHƯƠNG THỨC QUẢN LÝ =====

    // Thêm nhân viên
    public static void themNhanVien() {
        System.out.print("Nhap ten nhan vien: ");
        String ten = sc.nextLine();

        System.out.print("Nhap so CMND: ");
        String cmnd = sc.nextLine();

        System.out.print("Nhap so dien thoai: ");
        String sdt = sc.nextLine();

        System.out.print("Nhap luong co ban: ");
        double luong = Double.parseDouble(sc.nextLine());

        System.out.print("Nhap chuc vu: ");
        String chucvu = sc.nextLine();

        NhanVien nv = new NhanVien(ten, cmnd, sdt, luong,chucvu);
        dsNV.add(nv);
        System.out.println("Da them nhan vien thanh cong!");
    }

    // Xóa nhân viên
    public static void xoaNhanVien() {
        System.out.print("Nhap ma ID nhan vian can xoa: ");
        String id = sc.nextLine();

        NhanVien found = null;
        for (NhanVien nv : dsNV) {
            if (nv.getMaID().equals(id)) {
                found = nv;
                break;
            }
        }

        if (found != null) {
            dsNV.remove(found);
            System.out.println("Da xoa nhan vien: " + id);
        } else {
            System.out.println("Khong tim thay nhan vien co ID: " + id);
        }
    }

    // Sửa thông tin nhân viên
    public static void suaNhanVien() {
        System.out.print("Nhap ma ID nhan vien can sua: ");
        String id = sc.nextLine();

        NhanVien found = null;
        for (NhanVien nv : dsNV) {
            if (nv.getMaID().equals(id)) {
                found = nv;
                break;
            }
        }

        if (found == null) {
            System.out.println("Khong tim thay nhan vien co ID: " + id);
            return;
        }

        System.out.print("Nhap ten moi: ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) found.setTen(ten);

        System.out.print("Nhap CMND moi: ");
        String cmnd = sc.nextLine();
        if (!cmnd.isEmpty()) found.setSoCMND(cmnd);

        System.out.print("Nhap SDT moi: ");
        String sdt = sc.nextLine();
        if (!sdt.isEmpty()) found.setSoDienThoai(sdt);

        System.out.print("Nhap luong moi: ");
        String luongTxt = sc.nextLine();
        if (!luongTxt.isEmpty()) {
            try {
                double luong = Double.parseDouble(luongTxt);
                found.setLuongCoBan(luong);
            } catch (NumberFormatException e) {
                System.out.println("Luong khong hop le, giu nguyen gia tri cu.");
            }
        }
        System.out.print("Nhap chuc vu moi: ");
        System.out.println("Da cap nhat thong tin nhan vien!");
    }

    // Xem danh sách nhân viên
    public static void xemNhanVien() {
        if (dsNV.isEmpty()) {
            System.out.println("Danh sach nhan vien trong!");
            return;
        }

        System.out.println("\n===== DANH SACH NHAN VIEN =====");
        for (NhanVien nv : dsNV) {
            System.out.println(nv);
        }
    }
}