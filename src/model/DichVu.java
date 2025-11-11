package model;

public class DichVu {
    private String tenDichVu;
    private double giaDichVu;
    public DichVu(String tenDichVu, double giaDichVu) {
        setTenDichVu(tenDichVu);
        setGiaDichVu(giaDichVu);
    }
    public String getTenDichVu() {
        return tenDichVu;
    }
    public double getGiaDichVu() {
        return giaDichVu;
    }
    public void setTenDichVu(String tenDichVu) {
        if(tenDichVu != null){
            this.tenDichVu = tenDichVu;
        }
    }
    public void setGiaDichVu(double giaDichVu) {
        if(giaDichVu > 0){
            this.giaDichVu = giaDichVu;
        }
    }

}