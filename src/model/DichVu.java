package model;

public class DichVu {
    private int maDV;       // int để khớp với auto_increment trong DB
    private String tenDV;   // Khớp tên cột TenDV
    private double giaDV;   // Khớp tên cột GiaDV
    private String maPhong; // Bắt buộc có vì DB để Not Null

    public DichVu() {
    }

    // Constructor dùng khi thêm mới (Không cần maDV vì tự tăng)
    public DichVu(String tenDV, double giaDV, String maPhong) {
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        this.maPhong = maPhong;
    }

    // Constructor đầy đủ (khi đọc từ DB)
    public DichVu(int maDV, String tenDV, double giaDV, String maPhong) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        this.maPhong = maPhong;
    }

    public int getMaDV() { return maDV; }
    public void setMaDV(int maDV) { this.maDV = maDV; }

    public String getTenDV() { return tenDV; }
    public void setTenDV(String tenDV) { this.tenDV = tenDV; }

    public double getGiaDV() { return giaDV; }
    public void setGiaDV(double giaDV) { this.giaDV = giaDV; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    @Override
    public String toString() {
        return tenDV;
    }
}