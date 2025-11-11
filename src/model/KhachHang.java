package model;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class KhachHang extends Person {
    private String email;

    // Ghi chú: Các trường static này sẽ không còn được sử dụng
    // khi bạn chuyển hoàn toàn sang Repository
    private static List<KhachHang> dsKhachHang = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    // ===== CONSTRUCTORS =====

    // (MỚI) 1. Constructor rỗng: Cần thiết cho Repository
    public KhachHang() {
        super(); // Gọi constructor rỗng của Person
    }

    // (CŨ) 2. Constructor đầy đủ (khi thêm mới)
    public KhachHang(String ten, String soCMND, String soDienThoai, String email) {
        super("KH", ten, soCMND, soDienThoai); //
        setEmail(email);
    }

    // ===== GETTER & SETTER =====
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

    public static void themKhachHang(){
        System.out.print("Nhap ten khach hang: ");
        String ten = sc.nextLine();

        System.out.print("Nhap so CMND: ");
        String cmnd = sc.nextLine();


        System.out.print("Nhap so dien thoai: ");
        String sdt = sc.nextLine();

        System.out.print("Nhap email: ");
        String email = sc.nextLine();

        KhachHang kh = new KhachHang(ten,cmnd,sdt,email);
        dsKhachHang.add(kh);
        System.out.println("Them khach hang thanh cong");
    }

    public static void xoaKhachHang(){
        System.out.print("Nhap ma khach hang can xoa: ");
        String id = sc.nextLine();

        KhachHang found = null;
        for(KhachHang kh : dsKhachHang){
            if(kh.getMaID().equals(id)){
                found = kh;
                break;
            }
        }

        if(found != null){
            dsKhachHang.remove(found);
            System.out.println("Da xoa khach hang " + id);
        }else{
            System.out.println("Khong tim thay khach hang " + id);
        }
    }

    public static void suaKhachHang() {
        System.out.print("Nhap ma khach hang can sua: ");
        String id = sc.nextLine();

        KhachHang kh = null;
        for (KhachHang k : dsKhachHang) {
            if (k.getMaID().equals(id)) {
                kh = k;
                break;
            }
        }

        if (kh == null) {
            System.out.println("Khong tim thay khach hang nao co ma " + id);
            return;
        }

        System.out.println("Nhap thong tin moi:");

        System.out.print("Ten moi: ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) kh.setTen(ten);

        System.out.print("CMND moi: ");
        String cmnd = sc.nextLine();
        if (!cmnd.isEmpty()) kh.setSoCMND(cmnd);

        System.out.print("SDT moi: ");
        String sdt = sc.nextLine();
        if (!sdt.isEmpty()) kh.setSoDienThoai(sdt);

        System.out.print("Email moi: ");
        String email = sc.nextLine();
        if (!email.isEmpty()) kh.setEmail(email);

        System.out.println("Cap nhat thanh cong!");
    }

    public static void xemKhachHang() {
        if (dsKhachHang.isEmpty()) {
            System.out.println("Chua co khach hang nao!");
            return;
        }

        System.out.println("\n===== DANH SACH KHACH HANG =====");
        for (KhachHang kh : dsKhachHang) {
            System.out.println(kh);
        }
        System.out.println("================================\n");
    }

    public static void timKhachHang() {
        if (dsKhachHang.isEmpty()) {
            System.out.println(" Chua co khach hang nao!");
            return;
        }
        System.out.print("Vui long nhap ten khach hang: ");
        String ten = sc.nextLine().trim().toLowerCase();
        boolean found = false;
        for (KhachHang kh : dsKhachHang) {
            if (kh.getTen().toLowerCase().contains(ten)) {
                System.out.println(kh);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay khach hang co ten: " + ten);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Khach hang [ID=%s, Ten=%s, CMND=%s, SDT=%s, Email=%s]",
                getMaID(), getTen(), getSoCMND(), getSoDienThoai(), getEmail()
        );
    }

}