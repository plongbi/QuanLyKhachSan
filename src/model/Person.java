package model;

public class Person {
    private String maID;
    private String ten;
    private String soCMND;
    private String soDienThoai;

    protected static int cnt = 1;

    // ===== CONSTRUCTORS =====

    // (MỚI) 1. Constructor rỗng: Cần thiết cho Repository
    public Person() {
        // Để trống cho việc mapping dữ liệu từ CSDL
    }

    // (CŨ) 2. Constructor tự tạo ID tăng dần
    public Person(String prefix) {
        this.maID = String.format("%s%03d", prefix, cnt++);
    }

    // (CŨ) 3. Constructor đầy đủ thông tin (khi thêm mới)
    public Person(String prefix, String ten, String soCMND, String soDienThoai) {
        this(prefix); // Gọi constructor (2) để tự tạo maID
        setTen(ten);
        setSoCMND(soCMND);
        setSoDienThoai(soDienThoai);
    }

    // ===== GETTER =====
    public String getMaID() {
        return maID;
    }

    public String getTen() {
        return ten;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    // ===== SETTER =====

    // (MỚI) Rất quan trọng: Thêm hàm này
    // để Repository có thể set ID khi đọc từ CSDL
    public void setMaID(String maID) {
        this.maID = maID;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public static void setCnt(int cnt) {
        Person.cnt = cnt;
    }
}